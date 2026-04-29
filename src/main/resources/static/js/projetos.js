import { projectService } from "./projectService.js";

const VALID_CATEGORIES = ["Web", "Mobile", "Desktop", "Embarcados"];
const TITLE_BY_CATEGORY = {
    Todos: "Todos os Projetos",
    Web: "Projetos Web",
    Mobile: "Projetos Mobile",
    Desktop: "Projetos Desktop",
    Embarcados: "Sistemas Embarcados",
};

const SVG = {
    folder: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><path d="M22 19a2 2 0 0 1-2 2H4a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h5l2 3h9a2 2 0 0 1 2 2z"/></svg>',
    edit: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>',
    delete: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 6h18M19 6 18 20H6L5 6m5 0V4h4v2"/></svg>',
};

const state = {
    projects: [],
    filterCategory: "Todos",
    searchQuery: "",
    editingId: null,
    deletingId: null,
    viewMode: "grid",
    isLoading: false,
};

const setOpen = (selector, open) => {
    const el = document.querySelector(selector);
    if (el) {
        el.classList.toggle("open", open);
        el.setAttribute("aria-hidden", !open);
    }
};

const findProjectById = (id) =>
    state.projects.find((p) => p.id_projeto === id || p.id === id);

function bindOverlayClose(overlaySelector, closeFn) {
    document.querySelector(overlaySelector).addEventListener("click", (e) => {
        if (e.target.id === overlaySelector.slice(1)) closeFn();
    });
}

function getFilteredProjects() {
    const q = state.searchQuery.toLowerCase().trim();
    return state.projects.filter((p) => {
        const okCategory =
            state.filterCategory === "Todos" ||
            p.categoria === state.filterCategory;
        const okSearch =
            !q ||
            (p.nome && p.nome.toLowerCase().includes(q)) ||
            (p.descricao && p.descricao.toLowerCase().includes(q));
        return okCategory && okSearch;
    });
}

function categoryTag(c) {
    return `<span class="card-cat-tag tag-${c}">${c}</span>`;
}

function renderGrid(list) {
    const grid = document.querySelector("#items-grid");
    if (!list.length) {
        grid.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">${SVG.folder}</div>
                <h3 class="empty-title">Nenhum projeto encontrado</h3>
                <p class="empty-sub">Crie um novo projeto para começar.</p>
                <button class="empty-cta" onclick="openCreateProjectModal()">+ Novo Projeto</button>
            </div>`;
        return;
    }

    grid.innerHTML = list
        .map(
            (p) => `
        <article class="item-card">
            <div class="item-body">
                <div style="margin-bottom: 8px;">${categoryTag(p.categoria)}</div>
                <h3 class="item-name" title="${p.nome}">${p.nome}</h3>
                <p class="item-type" style="display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden;">${p.descricao}</p>
            </div>
            <div class="item-actions">
                <button class="action-btn action-edit" onclick="openEditProjectModal(${p.id_projeto || p.id})" title="Editar">${SVG.edit}</button>
                <button class="action-btn action-delete" onclick="openDeleteProjectModal(${p.id_projeto || p.id})" title="Deletar">${SVG.delete}</button>
            </div>
        </article>
    `,
        )
        .join("");
}

function renderTable(list) {
    const tbody = document.querySelector("#table-body");
    if (!list.length) {
        tbody.innerHTML = `
            <tr><td colspan="4">
                <div class="empty-state">
                    <div class="empty-icon">${SVG.folder}</div>
                    <h3 class="empty-title">Nenhum projeto encontrado</h3>
                </div>
            </td></tr>`;
        return;
    }

    tbody.innerHTML = list
        .map(
            (p) => `
        <tr>
            <td class="td-name"><strong>${p.nome}</strong></td>
            <td>${categoryTag(p.categoria)}</td>
            <td class="td-mono" style="max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;">${p.descricao}</td>
            <td>
                <div class="td-actions">
                    <button class="table-btn edit" onclick="openEditProjectModal(${p.id_projeto || p.id})" title="Editar">${SVG.edit}</button>
                    <button class="table-btn delete" onclick="openDeleteProjectModal(${p.id_projeto || p.id})" title="Deletar">${SVG.delete}</button>
                </div>
            </td>
        </tr>
    `,
        )
        .join("");
}

function render() {
    const list = getFilteredProjects();
    document.querySelector("#count-badge").textContent =
        `${list.length} ${list.length === 1 ? "projeto" : "projetos"}`;
    document.querySelector("#toolbar-title").textContent =
        TITLE_BY_CATEGORY[state.filterCategory] || state.filterCategory;

    if (state.viewMode === "grid") renderGrid(list);
    else renderTable(list);
}

function clearProjectForm() {
    document.querySelector("#p-name").value = "";
    document.querySelector("#p-cat").value = "";
    document.querySelector("#p-desc").value = "";

    document
        .querySelectorAll("#form-project small")
        .forEach((el) => el.setAttribute("hidden", "true"));
    document
        .querySelectorAll(
            "#form-project input, #form-project select, #form-project textarea",
        )
        .forEach((el) => el.removeAttribute("aria-invalid"));
}

function openCreateProjectModal() {
    state.editingId = null;
    clearProjectForm();
    document.querySelector("#project-modal-title").textContent =
        "Criar Novo Projeto";
    setOpen("#project-overlay", true);
}

function openEditProjectModal(id) {
    const p = findProjectById(id);
    if (!p) return;

    state.editingId = id;
    document.querySelector("#p-name").value = p.nome;
    document.querySelector("#p-cat").value = p.categoria;
    document.querySelector("#p-desc").value = p.descricao;

    document.querySelector("#project-modal-title").textContent =
        "Editar Projeto";
    setOpen("#project-overlay", true);
}

function closeProjectModal() {
    setOpen("#project-overlay", false);
    state.editingId = null;
    clearProjectForm();
}

const sanitize = (text) => text.replace(/[<>]/g, "").trim();

function validateProjectForm() {
    const nome = sanitize(document.querySelector("#p-name").value);
    const categoria = sanitize(document.querySelector("#p-cat").value);
    const descricao = sanitize(document.querySelector("#p-desc").value);

    if (!nome || nome.length < 5 || !categoria || !descricao) {
        showToast("Preencha todos os campos corretamente.", "error");
        return null;
    }

    //alterado p portugues
    return { nome, categoria, descricao };
}

async function saveProject() {
    if (state.isLoading) return;

    const payload = validateProjectForm();
    if (!payload) return;

    state.isLoading = true;
    const btn = document.querySelector("#btn-save-project");
    const originalText = btn.textContent;
    btn.textContent = "Salvando...";
    btn.disabled = true;

    try {
        if (state.editingId) {
            await projectService.atualizar(state.editingId, payload);
            showToast("Projeto atualizado!", "success");
        } else {
            await projectService.salvar(payload);
            showToast("Projeto criado com sucesso!", "success");
        }

        closeProjectModal();
        await loadProjects();
    } catch (err) {
        console.error(err);
        showToast(err.message || "Erro ao salvar projeto", "error");
    } finally {
        state.isLoading = false;
        btn.textContent = originalText;
        btn.disabled = false;
    }
}

async function confirmDeleteProject() {
    if (state.deletingId == null || state.isLoading) return;
    const p = findProjectById(state.deletingId);

    state.isLoading = true;
    try {
        await projectService.excluir(state.deletingId);

        cancelDelete();
        showToast(`${p ? `"${p.nome}"` : "Projeto"} deletado`, "warning");
        await loadProjects();
    } catch (err) {
        console.error(err);
        showToast(err.message || "Erro ao deletar", "error");
    } finally {
        state.isLoading = false;
    }
}

function switchViewMode(mode) {
    state.viewMode = mode;
    document
        .querySelector("#btn-grid")
        .classList.toggle("active", mode === "grid");
    document
        .querySelector("#btn-table")
        .classList.toggle("active", mode === "table");
    document.querySelector("#items-grid").hidden = mode !== "grid";
    document.querySelector("#items-table").hidden = mode !== "table";
    render();
}

function filterByCategory(category) {
    state.filterCategory = category;
    document
        .querySelectorAll(".cat-tab")
        .forEach((tab) =>
            tab.classList.toggle("active", tab.dataset.cat === category),
        );
    render();
}

function showToast(message, type = "success") {
    const t = document.createElement("div");
    t.className = `toast ${type}`;
    t.innerHTML = `<span class="toast-dot"></span>${message}`;
    document.querySelector("#toast-wrap").appendChild(t);
    setTimeout(() => {
        t.style.opacity = "0";
        t.style.transform = "translateY(6px)";
        t.style.transition = "opacity .2s, transform .2s";
        setTimeout(() => t.remove(), 220);
    }, 2600);
}

async function loadProjects() {
    try {
        const data = await projectService.listarTodos();
        state.projects = data || [];
    } catch (err) {
        console.error("API indisponível:", err);
        state.projects = []; 
        showToast("Não foi possível carregar os projetos", "error");
    }
    render();
}

window.openCreateProjectModal = openCreateProjectModal;
window.openEditProjectModal = openEditProjectModal;
window.openDeleteProjectModal = openDeleteProjectModal;

document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const cat = params.get("cat");
    if (cat && VALID_CATEGORIES.includes(cat)) filterByCategory(cat);

    loadProjects();

    const btnOpenProject = document.querySelector("#btn-open-project");
    if (btnOpenProject) {
        btnOpenProject.addEventListener("click", (e) => {
            e.preventDefault();
            openCreateProjectModal();
        });
    }

    document
        .querySelector("#btn-save-project")
        .addEventListener("click", saveProject);
    document
        .querySelector("#btn-cancel-project")
        .addEventListener("click", closeProjectModal);
    document
        .querySelector("#close-project-modal")
        .addEventListener("click", closeProjectModal);
    bindOverlayClose("#project-overlay", closeProjectModal);

    document
        .querySelector("#confirm-del")
        .addEventListener("click", confirmDeleteProject);
    document
        .querySelector("#cancel-del")
        .addEventListener("click", cancelDelete);
    bindOverlayClose("#del-overlay", cancelDelete);

    document
        .querySelector("#btn-grid")
        .addEventListener("click", () => switchViewMode("grid"));
    document
        .querySelector("#btn-table")
        .addEventListener("click", () => switchViewMode("table"));
    document
        .querySelectorAll(".cat-tab")
        .forEach((tab) =>
            tab.addEventListener("click", () =>
                filterByCategory(tab.dataset.cat),
            ),
        );

    document.querySelector("#search-input").addEventListener("input", (e) => {
        state.searchQuery = e.target.value;
        render();
    });
});
