import { projectService } from './services/projectService.js';
import api from './services/api.js';

// --- ESTADO & UTILITÁRIOS ---
const state = {
  projects: [],
  items: [],
  editingId: null,
  deletingId: null,
  filter: 'TODOS',
  query: '',
  photo: ''
};

const $ = (selector) => document.querySelector(selector);

const esc = (value) =>
  String(value ?? '').replace(/[&<>"']/g, (char) => ({
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;'
  }[char]));

const categoryLabel = (value) => {
  const map = { WEB: 'Web', MOBILE: 'Mobile', DESKTOP: 'Desktop', EMBARCADOS: 'Embarcados' };
  return map[value] || value;
};

const icon = {
  photo: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="3"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/></svg>',
  edit: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="m18.5 2.5 3 3L12 15l-4 1 1-4 10.5-9.5z"/></svg>',
  delete: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 6h18M19 6 18 20H6L5 6m5 0V4h4v2"/></svg>'
};

// --- MODAIS & NOTIFICAÇÕES ---
function openOverlay(id, open) {
  const element = $(id);
  if (!element) return;
  element.classList.toggle('open', open);
  element.setAttribute('aria-hidden', String(!open));
}

function toast(message, type = 'success') {
  const element = document.createElement('div');
  element.className = `toast ${type}`;
  element.innerHTML = `<span class="toast-dot"></span>${esc(message)}`;
  
  const container = $('#toast-wrap');
  if (container) container.append(element);
  
  setTimeout(() => element.remove(), 2800);
}

// --- HELPERS DE PROJETO ---
function projectId(project) {
  return project.id ?? project.id_projeto;
}

function findProject(id) {
  return state.projects.find((p) => String(projectId(p)) === String(id));
}

function filtered() {
  const query = state.query.toLowerCase().trim();
  
  return state.projects.filter((project) => {
    const category = project.categoriaProjeto || project.categoria;
    const matchesFilter = state.filter === 'TODOS' || category === state.filter;
    const matchesQuery = !query || `${project.nome} ${project.descricao}`.toLowerCase().includes(query);
    
    return matchesFilter && matchesQuery;
  });
}

function selectedItems() {
  return [...document.querySelectorAll('.project-item-row')]
    .map((row) => ({
      idItem: Number(row.dataset.id),
      quantidadeUsada: Number(row.querySelector('input').value)
    }))
    .filter((item) => item.quantidadeUsada > 0);
}

// --- RENDERIZAÇÃO ---
function renderPicker(selected = []) {
  const quantities = new Map(selected.map((item) => [item.idItem, item.quantidadeUsada]));
  const picker = $('#project-items-picker');
  
  if (!state.items.length) {
    picker.innerHTML = '<p>Nenhum item cadastrado.</p>';
    return;
  }

  picker.innerHTML = state.items.map((item) => `
    <div class="project-item-row" data-id="${item.id}">
      <span>${esc(item.nome)}</span>
      <button type="button" class="qty-minus" aria-label="Diminuir">-</button>
      <input type="number" min="0" max="${item.quantidade}" value="${quantities.get(item.id) || 0}">
      <button type="button" class="qty-plus" aria-label="Aumentar">+</button>
      <small>disp. ${item.quantidade}</small>
    </div>
  `).join('');
}

function render() {
  const projects = filtered();
  $('#count-badge').textContent = `${projects.length} ${projects.length === 1 ? 'projeto' : 'projetos'}`;

  if (!projects.length) {
    $('#items-grid').innerHTML = `
      <div class="empty-state">
        <div class="empty-icon">${icon.photo}</div>
        <h3 class="empty-title">Nenhum projeto encontrado</h3>
        <p class="empty-sub">Crie um novo projeto para começar.</p>
      </div>
    `;
    return;
  }

  $('#items-grid').innerHTML = projects.map((project) => {
    const cat = categoryLabel(project.categoriaProjeto || project.categoria);
    const photoContent = project.imagem 
      ? `<img src="${esc(project.imagem)}" alt="${esc(project.nome)}">` 
      : icon.photo;

    return `
      <article class="item-card">
        <div class="item-photo">
          ${photoContent}
          <span class="card-cat-tag">${esc(cat)}</span>
        </div>
        <div class="item-body">
          <h3 class="item-name">${esc(project.nome)}</h3>
          <p class="item-type">${esc(project.descricao)}</p>
          <div class="item-meta-row">
            <span class="item-qty"><strong>${project.itens?.length || 0}</strong> itens</span>
          </div>
        </div>
        <div class="item-actions">
          <button class="action-btn" data-view="${projectId(project)}" title="Ver detalhes">Ver</button>
          <button class="action-btn action-edit" data-edit="${projectId(project)}" title="Editar">${icon.edit}</button>
          <button class="action-btn action-delete" data-delete="${projectId(project)}" title="Excluir">${icon.delete}</button>
        </div>
      </article>
    `;
  }).join('');
}

// --- CONTROLE DE FORMULÁRIO / MODAIS ---
function clearForm() {
  $('#form-project').reset();
  state.photo = '';
  const preview = $('#p-image-preview');
  preview.src = '';
  preview.hidden = true;
  renderPicker();
}

function openCreate() {
  state.editingId = null;
  clearForm();
  $('#project-modal-title').textContent = 'Novo Projeto';
  openOverlay('#project-overlay', true);
}

function openEdit(id) {
  const project = findProject(id);
  if (!project) return;

  state.editingId = id;
  $('#p-name').value = project.nome;
  $('#p-cat').value = project.categoriaProjeto || project.categoria;
  $('#p-desc').value = project.descricao;
  
  state.photo = project.imagem || '';
  const preview = $('#p-image-preview');
  if (state.photo) {
    preview.src = state.photo;
    preview.hidden = false;
  } else {
    preview.hidden = true;
  }

  renderPicker(project.itens || []);
  $('#project-modal-title').textContent = 'Editar Projeto';
  openOverlay('#project-overlay', true);
}

function openView(id) {
  const project = findProject(id);
  if (!project) return;

  $('#view-project-name').textContent = project.nome;
  $('#view-project-category').textContent = categoryLabel(project.categoriaProjeto || project.categoria);
  $('#view-project-description').textContent = project.descricao;
  
  const img = $('#view-project-image');
  img.src = project.imagem || '';
  img.hidden = !project.imagem;

  $('#view-project-items').innerHTML = project.itens?.length
    ? project.itens.map((i) => `<div class="linked-item"><span>${esc(i.nome)}</span><strong>${i.quantidadeUsada} un.</strong></div>`).join('')
    : '<p>Nenhum item vinculado.</p>';

  openOverlay('#view-overlay', true);
}

function readPhoto(file) {
  if (!file) return;
  const reader = new FileReader();
  reader.onload = () => {
    state.photo = reader.result;
    const preview = $('#p-image-preview');
    preview.src = state.photo;
    preview.hidden = false;
  };
  reader.readAsDataURL(file);
}

function confirmDelete(id) {
  state.deletingId = id;
  const project = findProject(id);
  $('#del-item-name').textContent = project ? `"${project.nome}"` : '';
  openOverlay('#del-overlay', true);
}

// --- OPERAÇÕES NA API ---
async function load() {
  try {
    const [projectsRes, itemsRes] = await Promise.all([
      projectService.listarTodos(),
      api.get('/api/items')
    ]);
    state.projects = projectsRes;
    state.items = itemsRes;
  } catch (error) {
    state.projects = [];
    toast('Não foi possível carregar os projetos.', 'error');
  }
  render();
}

async function save() {
  const payload = {
    nome: $('#p-name').value.trim(),
    categoria: $('#p-cat').value,
    descricao: $('#p-desc').value.trim(),
    imagem: state.photo,
    itens: selectedItems()
  };

  if (!payload.nome || !payload.categoria || !payload.descricao) {
    return toast('Preencha os campos obrigatórios.', 'error');
  }

  try {
    if (state.editingId) {
      await projectService.atualizar(state.editingId, payload);
    } else {
      await projectService.salvar(payload);
    }
    openOverlay('#project-overlay', false);
    await load();
    toast('Projeto salvo com sucesso.');
  } catch (error) {
    toast(error.message || 'Erro ao salvar projeto.', 'error');
  }
}

async function remove() {
  try {
    await projectService.excluir(state.deletingId);
    openOverlay('#del-overlay', false);
    await load();
    toast('Projeto excluído.', 'warning');
  } catch (error) {
    toast(error.message || 'Erro ao excluir projeto.', 'error');
  }
}

// --- EVENTOS ---
document.addEventListener('DOMContentLoaded', () => {
  load();

  // Modais & Form
  $('#btn-open-project').onclick = openCreate;
  $('#btn-save-project').onclick = save;
  $('#btn-cancel-project').onclick = () => openOverlay('#project-overlay', false);
  $('#close-project-modal').onclick = () => openOverlay('#project-overlay', false);
  $('#close-view-modal').onclick = () => openOverlay('#view-overlay', false);
  
  // Delete
  $('#cancel-del').onclick = () => openOverlay('#del-overlay', false);
  $('#confirm-del').onclick = remove;

  // Inputs
  $('#p-image').onchange = (e) => readPhoto(e.target.files[0]);
  $('#search-input').oninput = (e) => {
    state.query = e.target.value;
    render();
  };

  // Categorias (Filtro)
  document.querySelectorAll('.cat-tab').forEach((tab) => {
    tab.onclick = () => {
      state.filter = tab.dataset.cat === 'Todos' ? 'TODOS' : tab.dataset.cat.toUpperCase();
      document.querySelectorAll('.cat-tab').forEach((t) => t.classList.toggle('active', t === tab));
      render();
    };
  });

  // Delegação de cliques - Lista de Projetos
  $('#items-grid').onclick = (e) => {
    const btn = e.target.closest('button');
    if (!btn) return;
    if (btn.dataset.view) openView(btn.dataset.view);
    if (btn.dataset.edit) openEdit(btn.dataset.edit);
    if (btn.dataset.delete) confirmDelete(btn.dataset.delete);
  };

  // Delegação de cliques - Seletor de Quantidade
  $('#project-items-picker').onclick = (e) => {
    const row = e.target.closest('.project-item-row');
    if (!row) return;

    const input = row.querySelector('input');
    const max = Number(input.max);
    const delta = e.target.classList.contains('qty-plus') ? 1 : e.target.classList.contains('qty-minus') ? -1 : 0;
    
    if (delta !== 0) {
      input.value = Math.min(max, Math.max(0, Number(input.value) + delta));
    }
  };
});