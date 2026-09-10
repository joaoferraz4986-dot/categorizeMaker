import { itemService } from "./services/itemService.js";
import { handlePhotoUpload } from "./img-base64.js";

const VALID_CATEGORIES = ["FERRAMENTAS", "COMPONENTES", "UTILITARIOS"];
const TITLE_BY_CATEGORY = {
  TODOS: "Todos os Itens",
  FERRAMENTAS: "Ferramentas",
  COMPONENTES: "Componentes",
  UTILITARIOS: "Utilitários",
};

const SVG = {
  photo:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="3"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/></svg>',
  edit: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>',
  delete:
    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 6h18M19 6 18 20H6L5 6m5 0V4h4v2"/></svg>',
};

const state = {
  items: [],
  totalItems: 0,
  filterCategory: "TODOS",
  filterState: null,
  searchQuery: "",
  editingId: null,
  deletingId: null,
  viewMode: "grid",
  pendingPhoto: "",
  searchTimer: null,
};

const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => document.querySelectorAll(selector);
const esc = (value) =>
  String(value ?? "").replace(
    /[&<>"']/g,
    (char) =>
      ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;" })[
        char
      ],
  );
const setOpen = (selector, open) => {
  const element = $(selector);
  if (!element) return;
  element.classList.toggle("open", open);
  element.setAttribute("aria-hidden", String(!open));
};
const findItemById = (id) =>
  state.items.find((item) => String(item.id) === String(id));

function bindOverlayClose(selector, closeFn) {
  const overlay = $(selector);
  overlay?.addEventListener("click", (event) => {
    if (event.target === overlay) closeFn();
  });
}

function currentFilters() {
  return {
    name: state.searchQuery.trim(),
    category: state.filterCategory === "TODOS" ? "" : state.filterCategory,
    status: state.filterState || "",
  };
}

function stateLabel(value) {
  return (
    { LIVRE: "Livre", USADO: "Em uso", QUEBRADO: "Quebrado" }[value] ||
    value ||
    "-"
  );
}

function categoryLabel(value) {
  return (
    {
      FERRAMENTAS: "Ferramentas",
      COMPONENTES: "Componentes",
      UTILITARIOS: "Utilitários",
    }[value] ||
    value ||
    "-"
  );
}

function stateBadge(value) {
  return `<span class="state-badge state-${String(value || "").toLowerCase()}">${esc(stateLabel(value))}</span>`;
}

function categoryTag(value) {
  return `<span class="card-cat-tag tag-${esc(value)}">${esc(categoryLabel(value))}</span>`;
}

function photoEl(photo, category) {
  const tag = categoryTag(category);
  if (photo)
    return `<div class="item-photo"><img src="${esc(photo)}" alt="" />${tag}</div>`;
  return `<div class="item-photo">${tag}<div class="item-photo-placeholder">${SVG.photo}<span>Sem foto</span></div></div>`;
}

function openItemCard(id) {
  const item = findItemById(id);
  if (!item) return;
  $("#view-nome").textContent = item.nome || "-";
  $("#view-categoria").textContent = categoryLabel(item.categoria);
  $("#view-tipo").textContent = item.tipo || "-";
  $("#view-quantidade").textContent = `${item.quantidade ?? 0} un.`;
  $("#view-estado").innerHTML = stateBadge(item.estado);

  const photoContainer = $("#view-photo-container");
  photoContainer.innerHTML = item.imagem
    ? `<img src="${esc(item.imagem)}" alt="${esc(item.nome)}" />`
    : `<div class="item-photo-view-placeholder">${SVG.photo}<span>Sem foto</span></div>`;

  $("#view-btn-edit").onclick = () => {
    closeItemCard();
    openEditModal(id);
  };
  setOpen("#view-overlay", true);
}

function closeItemCard() {
  setOpen("#view-overlay", false);
}

function emptyState() {
  return `<div class="empty-state"><div class="empty-icon">${SVG.photo}</div><h3 class="empty-title">Nenhum item encontrado</h3><p class="empty-sub">Tente remover um filtro ou crie um novo item.</p><button class="empty-cta" id="empty-create">+ Novo item</button></div>`;
}

function renderGrid(list) {
  const grid = $("#items-grid");
  if (!list.length) {
    grid.innerHTML = emptyState();
    $("#empty-create")?.addEventListener("click", openCreateModal);
    return;
  }
  grid.innerHTML = list
    .map(
      (
        item,
      ) => `<article class="item-card" data-view-id="${esc(item.id)}" tabindex="0" title="Ver detalhes">
        ${photoEl(item.imagem, item.categoria)}
        <div class="item-body"><h3 class="item-name" title="${esc(item.nome)}">${esc(item.nome)}</h3><p class="item-type">${esc(item.tipo || "Tipo não informado")}</p><div class="item-meta-row"><span class="item-qty"><strong>${item.quantidade ?? 0}</strong> un.</span>${stateBadge(item.estado)}</div></div>
        <div class="item-actions"><button class="action-btn action-edit" data-edit-id="${esc(item.id)}" title="Editar">${SVG.edit}</button><button class="action-btn action-delete" data-delete-id="${esc(item.id)}" title="Deletar">${SVG.delete}</button></div>
    </article>`,
    )
    .join("");
}

function renderTable(list) {
  const tbody = $("#table-body");
  if (!list.length) {
    tbody.innerHTML = `<tr><td colspan="7">${emptyState()}</td></tr>`;
    $("#empty-create")?.addEventListener("click", openCreateModal);
    return;
  }
  tbody.innerHTML = list
    .map(
      (item) =>
        `<tr data-view-id="${esc(item.id)}" tabindex="0" title="Ver detalhes"><td><div class="td-thumb">${item.imagem ? `<img src="${esc(item.imagem)}" alt="" />` : SVG.photo}</div></td><td class="td-name">${esc(item.nome)}</td><td class="td-mono">${esc(item.tipo || "-")}</td><td>${esc(categoryLabel(item.categoria))}</td><td>${item.quantidade ?? 0}</td><td>${stateBadge(item.estado)}</td><td><div class="td-actions"><button class="table-btn edit" data-edit-id="${esc(item.id)}" title="Editar">${SVG.edit}</button><button class="table-btn delete" data-delete-id="${esc(item.id)}" title="Deletar">${SVG.delete}</button></div></td></tr>`,
    )
    .join("");
}

function render() {
  const list = state.items;
  $("#count-badge").textContent =
    `${state.totalItems} ${state.totalItems === 1 ? "item" : "itens"}`;
  $("#toolbar-title").textContent =
    TITLE_BY_CATEGORY[state.filterCategory] || "Itens";
  if (state.viewMode === "grid") renderGrid(list);
  else renderTable(list);
}

function clearFormFields() {
  ["f-categoria", "f-tipo", "f-nome", "f-quantidade", "f-estado"].forEach(
    (id) => {
      document.getElementById(id).value = "";
    },
  );
  $("#f-foto").value = "";
  $("#photo-preview").style.display = "none";
  $("#photo-hint").style.display = "block";
}

function openCreateModal() {
  state.editingId = null;
  state.pendingPhoto = "";
  clearFormFields();
  $("#modal-title").textContent = "Novo Item";
  setOpen("#form-overlay", true);
}

function openEditModal(id) {
  const item = findItemById(id);
  if (!item) return;
  state.editingId = id;
  state.pendingPhoto = item.imagem || "";
  $("#f-categoria").value = item.categoria || "";
  $("#f-tipo").value = item.tipo || "";
  $("#f-nome").value = item.nome || "";
  $("#f-quantidade").value = item.quantidade ?? "";
  $("#f-estado").value = item.estado || "";
  const preview = $("#photo-preview");
  const hint = $("#photo-hint");
  if (item.imagem) {
    preview.src = item.imagem;
    preview.style.display = "block";
    hint.style.display = "none";
  } else {
    preview.style.display = "none";
    hint.style.display = "block";
  }
  $("#modal-title").textContent = "Editar Item";
  setOpen("#form-overlay", true);
}

function closeFormModal() {
  setOpen("#form-overlay", false);
  state.editingId = null;
  state.pendingPhoto = "";
  clearFormFields();
}

function validateForm() {
  const categoria = $("#f-categoria").value.trim();
  const tipo = $("#f-tipo").value.trim();
  const nome = $("#f-nome").value.trim();
  const quantidade = parseInt($("#f-quantidade").value, 10);
  const estado = $("#f-estado").value.trim();
  if (
    !categoria ||
    !tipo ||
    !nome ||
    Number.isNaN(quantidade) ||
    quantidade < 1 ||
    !estado
  ) {
    showToast("Preencha todos os campos corretamente", "error");
    return null;
  }
  return {
    nome,
    tipo,
    categoria,
    quantidade,
    estado,
    imagem: state.pendingPhoto,
  };
}

async function saveItem() {
  const payload = validateForm();
  if (!payload) return;
  try {
    if (state.editingId !== null)
      await itemService.atualizar(state.editingId, payload);
    else await itemService.salvar(payload);
    showToast(
      state.editingId !== null ? "Item atualizado" : "Item criado",
      "success",
    );
    closeFormModal();
    await loadItems();
  } catch (error) {
    console.error(error);
    showToast(error.message || "Erro ao salvar item", "error");
  }
}

function openDeleteModal(id) {
  const item = findItemById(id);
  if (!item) return;
  state.deletingId = id;
  $("#del-item-name").textContent = `"${item.nome}"`;
  setOpen("#del-overlay", true);
}
function cancelDelete() {
  setOpen("#del-overlay", false);
  state.deletingId = null;
}

async function confirmDelete() {
  if (state.deletingId == null) return;
  const item = findItemById(state.deletingId);
  try {
    await itemService.excluir(state.deletingId);
    cancelDelete();
    showToast(`${item ? `"${item.nome}"` : "Item"} foi deletado`, "warning");
    await loadItems();
  } catch (error) {
    console.error(error);
    showToast(error.message || "Erro ao deletar", "error");
  }
}

function switchViewMode(mode) {
  state.viewMode = mode;
  $("#btn-grid").classList.toggle("active", mode === "grid");
  $("#btn-table").classList.toggle("active", mode === "table");
  $("#items-grid").hidden = mode !== "grid";
  $("#items-table").hidden = mode !== "table";
  render();
}

async function loadItems() {
  try {
    const response = await itemService.listarTodos(currentFilters());
    state.items = response.items;
    state.totalItems = response.total;
  } catch (error) {
    console.warn("Não foi possível carregar os itens.", error);
    state.items = [];
    state.totalItems = 0;
  }
  render();
}

function scheduleLoad() {
  clearTimeout(state.searchTimer);
  state.searchTimer = setTimeout(loadItems, 250);
}

function filterByCategory(category) {
  state.filterCategory = category;
  $$(".cat-tab").forEach((tab) =>
    tab.classList.toggle("active", tab.dataset.cat === category),
  );
  loadItems();
}

function filterByState(value) {
  state.filterState = state.filterState === value ? null : value;
  $$(".state-pill").forEach((pill) => {
    pill.classList.remove("active-livre", "active-usado", "active-quebrado");
    if (state.filterState === pill.dataset.state)
      pill.classList.add(`active-${String(value).toLowerCase()}`);
  });
  loadItems();
}

function showToast(message, type = "success") {
  const toast = document.createElement("div");
  toast.className = `toast ${type}`;
  toast.innerHTML = `<span class="toast-dot"></span>${esc(message)}`;
  $("#toast-wrap").appendChild(toast);
  setTimeout(() => {
    toast.style.opacity = "0";
    toast.style.transform = "translateY(6px)";
    toast.style.transition = "opacity .2s, transform .2s";
    setTimeout(() => toast.remove(), 220);
  }, 2600);
}

function updatePdfSummary(filters) {
  const active = [
    filters.name ? `Nome: ${filters.name}` : "",
    filters.type ? `Tipo: ${filters.type}` : "",
    filters.category ? `Categoria: ${categoryLabel(filters.category)}` : "",
    filters.status ? `Estado: ${stateLabel(filters.status)}` : "",
  ].filter(Boolean);
  const summary = $("#pdf-current-filters");
  summary.hidden = active.length === 0;
  summary.innerHTML = active.length
    ? `<span class="summary-label">Filtros atuais</span>${active.map((label) => `<span class="filter-chip">${esc(label)}</span>`).join("")}`
    : "";
}

function openPdfModal() {
  const filters = currentFilters();
  $("#pdf-name").value = filters.name;
  $("#pdf-type").value = "";
  $("#pdf-category").value = filters.category;
  $("#pdf-status").value = filters.status;
  updatePdfSummary(filters);
  setOpen("#pdf-overlay", true);
  $("#pdf-name").focus();
}
function closePdfModal() {
  setOpen("#pdf-overlay", false);
}

async function generatePdf() {
  const button = $("#btn-generate-pdf");
  const filters = {
    name: $("#pdf-name").value.trim(),
    type: $("#pdf-type").value.trim(),
    category: $("#pdf-category").value,
    status: $("#pdf-status").value,
  };
  button.disabled = true;
  button.classList.add("is-loading");
  try {
    const blob = await itemService.exportarPdf(filters);
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = "relatorio-itens.pdf";
    document.body.appendChild(link);
    link.click();
    link.remove();
    URL.revokeObjectURL(url);
    closePdfModal();
    showToast("PDF gerado com os filtros escolhidos", "success");
  } catch (error) {
    console.error(error);
    showToast(error.message || "Erro ao gerar PDF", "error");
  } finally {
    button.disabled = false;
    button.classList.remove("is-loading");
  }
}

function delegatedItemActions(event) {
  const edit = event.target.closest("[data-edit-id]");
  if (edit) {
    event.stopPropagation();
    openEditModal(edit.dataset.editId);
    return;
  }
  const remove = event.target.closest("[data-delete-id]");
  if (remove) {
    event.stopPropagation();
    openDeleteModal(remove.dataset.deleteId);
    return;
  }
  const item = event.target.closest("[data-view-id]");
  if (item) openItemCard(item.dataset.viewId);
}

window.openCreateModal = openCreateModal;
window.openEditModal = openEditModal;
window.openDeleteModal = openDeleteModal;
window.openItemCard = openItemCard;

function initLab() {
  const categoryParam = new URLSearchParams(window.location.search).get("cat");
  const category = String(categoryParam || "").toUpperCase();
  if (VALID_CATEGORIES.includes(category)) filterByCategory(category);
  else loadItems();

  $("#btn-open-create").addEventListener("click", openCreateModal);
  $("#btn-save").addEventListener("click", saveItem);
  $("#btn-cancel").addEventListener("click", closeFormModal);
  $("#close-modal").addEventListener("click", closeFormModal);
  bindOverlayClose("#form-overlay", closeFormModal);
  $("#confirm-del").addEventListener("click", confirmDelete);
  $("#cancel-del").addEventListener("click", cancelDelete);
  bindOverlayClose("#del-overlay", cancelDelete);
  $("#close-view-modal").addEventListener("click", closeItemCard);
  bindOverlayClose("#view-overlay", closeItemCard);
  $("#close-pdf-modal").addEventListener("click", closePdfModal);
  $("#btn-cancel-pdf").addEventListener("click", closePdfModal);
  $("#btn-generate-pdf").addEventListener("click", generatePdf);
  bindOverlayClose("#pdf-overlay", closePdfModal);
  $("#f-foto").addEventListener("change", (event) =>
    handlePhotoUpload(event, state, showToast, $),
  );
  $("#btn-grid").addEventListener("click", () => switchViewMode("grid"));
  $("#btn-table").addEventListener("click", () => switchViewMode("table"));
  $$(".cat-tab").forEach((tab) =>
    tab.addEventListener("click", () => filterByCategory(tab.dataset.cat)),
  );
  $$(".state-pill").forEach((pill) =>
    pill.addEventListener("click", () => filterByState(pill.dataset.state)),
  );
  $("#search-input").addEventListener("input", (event) => {
    state.searchQuery = event.target.value;
    scheduleLoad();
  });
  $("#items-grid").addEventListener("click", delegatedItemActions);
  $("#items-table").addEventListener("click", delegatedItemActions);
  $("#items-grid").addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
      const item = event.target.closest("[data-view-id]");
      if (item) openItemCard(item.dataset.viewId);
    }
  });
  $("#items-table").addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
      const item = event.target.closest("[data-view-id]");
      if (item) openItemCard(item.dataset.viewId);
    }
  });
}

if (document.readyState === "loading")
  document.addEventListener("DOMContentLoaded", initLab, { once: true });
else initLab();

$("#btn-open-pdf")?.addEventListener("click", openPdfModal);
