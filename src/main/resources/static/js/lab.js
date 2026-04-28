const API_BASE_URL = '/api/items';
const VALID_CATEGORIES = ['Ferramentas', 'Componentes', 'Utilitarios'];
const TITLE_BY_CATEGORY = {
    Todos: 'Todos os Itens',
    Ferramentas: 'Ferramentas',
    Componentes: 'Componentes',
    Utilitarios: 'Utilitarios'
};

const SVG = {
    photo: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5"><rect x="3" y="3" width="18" height="18" rx="3"/><circle cx="8.5" cy="8.5" r="1.5"/><path d="m21 15-5-5L5 21"/></svg>',
    edit: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg>',
    delete: '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 6h18M19 6 18 20H6L5 6m5 0V4h4v2"/></svg>'
};

const state = {
    items: [],
    filterCategory: 'Todos',
    filterState: null,
    searchQuery: '',
    editingId: null,
    deletingId: null,
    viewMode: 'grid',
    pendingPhoto: ''
};

const $ = (sel) => document.querySelector(sel);
const $$ = (sel) => document.querySelectorAll(sel);

const setOpen = (selector, open) => $(selector).classList.toggle('open', open);
const findItemById = (id) => state.items.find((item) => item.id === id);

function bindOverlayClose(overlaySelector, closeFn) {
    $(overlaySelector).addEventListener('click', (e) => {
        if (e.target.id === overlaySelector.slice(1)) closeFn();
    });
}

function getFilteredItems() {
    const q = state.searchQuery.toLowerCase().trim();
    return state.items.filter((item) => {
        const okCategory = state.filterCategory === 'Todos' || item.categoria === state.filterCategory;
        const okState = !state.filterState || item.estado === state.filterState;
        const okSearch = !q
            || (item.nome && item.nome.toLowerCase().includes(q))
            || (item.tipo && item.tipo.toLowerCase().includes(q));
        return okCategory && okState && okSearch;
    });
}

function stateBadge(s) {
    return `<span class="state-badge state-${s}">${s}</span>`;
}

function categoryTag(c) {
    return `<span class="card-cat-tag tag-${c}">${c}</span>`;
}

function photoEl(photo, category) {
    const tag = category ? categoryTag(category) : '';
    if (photo) return `<div class="item-photo"><img src="${photo}" alt="" />${tag}</div>`;
    return `<div class="item-photo">${tag}<div class="item-photo-placeholder">${SVG.photo}<span>Sem foto</span></div></div>`;
}

function renderGrid(list) {
    const grid = $('#items-grid');
    if (!list.length) {
        grid.innerHTML = `
            <div class="empty-state">
                <div class="empty-icon">${SVG.photo}</div>
                <h3 class="empty-title">Nenhum item encontrado</h3>
                <p class="empty-sub">Crie um novo item para comecar.</p>
                <button class="empty-cta" onclick="openCreateModal()">+ Novo item</button>
            </div>`;
        return;
    }

    grid.innerHTML = list.map((item) => `
        <article class="item-card">
            ${photoEl(item.imagem, item.categoria)}
            <div class="item-body">
                <h3 class="item-name" title="${item.nome}">${item.nome}</h3>
                <p class="item-type">${item.tipo}</p>
                <div class="item-meta-row">
                    <span class="item-qty"><strong>${item.quantidade}</strong> un.</span>
                    ${stateBadge(item.estado)}
                </div>
            </div>
            <div class="item-actions">
                <button class="action-btn action-edit" onclick="openEditModal(${item.id})" title="Editar">${SVG.edit}</button>
                <button class="action-btn action-delete" onclick="openDeleteModal(${item.id})" title="Deletar">${SVG.delete}</button>
            </div>
        </article>
    `).join('');
}

function renderTable(list) {
    const tbody = $('#table-body');
    if (!list.length) {
        tbody.innerHTML = `
            <tr><td colspan="7">
                <div class="empty-state">
                    <div class="empty-icon">${SVG.photo}</div>
                    <h3 class="empty-title">Nenhum item encontrado</h3>
                </div>
            </td></tr>`;
        return;
    }

    tbody.innerHTML = list.map((item) => `
        <tr>
            <td><div class="td-thumb">${item.imagem ? `<img src="${item.imagem}" alt="" />` : SVG.photo}</div></td>
            <td class="td-name">${item.nome}</td>
            <td class="td-mono">${item.tipo}</td>
            <td>${item.categoria}</td>
            <td>${item.quantidade}</td>
            <td>${stateBadge(item.estado)}</td>
            <td><div class="td-actions">
                <button class="table-btn edit" onclick="openEditModal(${item.id})" title="Editar">${SVG.edit}</button>
                <button class="table-btn delete" onclick="openDeleteModal(${item.id})" title="Deletar">${SVG.delete}</button>
            </div></td>
        </tr>
    `).join('');
}

function render() {
    const list = getFilteredItems();
    $('#count-badge').textContent = `${list.length} ${list.length === 1 ? 'item' : 'itens'}`;
    $('#toolbar-title').textContent = TITLE_BY_CATEGORY[state.filterCategory] || state.filterCategory;
    if (state.viewMode === 'grid') renderGrid(list);
    else renderTable(list);
}

function clearFormFields() {
    ['f-categoria', 'f-tipo', 'f-nome', 'f-quantidade', 'f-estado'].forEach((id) => {
        document.getElementById(id).value = '';
    });
    $('#f-foto').value = '';
    $('#photo-preview').style.display = 'none';
    $('#photo-hint').style.display = 'block';
}

function openCreateModal() {
    state.editingId = null;
    state.pendingPhoto = '';
    clearFormFields();
    $('#modal-title').textContent = 'Novo Item';
    setOpen('#form-overlay', true);
}

function openEditModal(id) {
    const item = findItemById(id);
    if (!item) return;

    state.editingId = id;
    state.pendingPhoto = item.imagem || '';
    $('#f-categoria').value = item.categoria;
    $('#f-tipo').value = item.tipo;
    $('#f-nome').value = item.nome;
    $('#f-quantidade').value = item.quantidade;
    $('#f-estado').value = item.estado;

    const preview = $('#photo-preview');
    const hint = $('#photo-hint');
    if (item.imagem) {
        preview.src = item.imagem;
        preview.style.display = 'block';
        hint.style.display = 'none';
    } else {
        preview.style.display = 'none';
        hint.style.display = 'block';
    }

    $('#modal-title').textContent = 'Editar Item';
    setOpen('#form-overlay', true);
}

function closeFormModal() {
    setOpen('#form-overlay', false);
    state.editingId = null;
    state.pendingPhoto = '';
    clearFormFields();
}

function validateForm() {
    const categoria = $('#f-categoria').value.trim();
    const tipo = $('#f-tipo').value.trim();
    const nome = $('#f-nome').value.trim();
    const quantidade = parseInt($('#f-quantidade').value, 10);
    const estado = $('#f-estado').value.trim();

    if (!categoria || !tipo || !nome || Number.isNaN(quantidade) || quantidade < 1 || !estado) {
        showToast('Preencha todos os campos corretamente', 'error');
        return null;
    }
    return { nome, tipo, categoria, quantidade, estado, imagem: state.pendingPhoto };
}

async function apiSave(payload) {
    const isEdit = state.editingId !== null;
    const url = isEdit ? `${API_BASE_URL}/${state.editingId}` : API_BASE_URL;
    const method = isEdit ? 'PUT' : 'POST';
    const res = await fetch(url, {
        method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });

    if (!res.ok) {
        let msg = 'Erro ao salvar item';
        try {
            const data = await res.json();
            msg = data.error || data.message || msg;
        } catch (_) {}
        throw new Error(msg);
    }
}

async function saveItem() {
    const payload = validateForm();
    if (!payload) return;
    try {
        await apiSave(payload);
        showToast(state.editingId !== null ? 'Item atualizado' : 'Item criado', 'success');
        await loadItems();
        closeFormModal();
    } catch (err) {
        console.error(err);
        showToast(err.message || 'Erro ao salvar item', 'error');
    }
}

function openDeleteModal(id) {
    const item = findItemById(id);
    if (!item) return;
    state.deletingId = id;
    $('#del-item-name').textContent = `"${item.nome}"`;
    setOpen('#del-overlay', true);
}

function cancelDelete() {
    setOpen('#del-overlay', false);
    state.deletingId = null;
}

async function apiDelete(id) {
    const res = await fetch(`${API_BASE_URL}/${id}`, { method: 'DELETE' });
    if (!res.ok) throw new Error('Erro ao deletar item');
}

async function confirmDelete() {
    if (state.deletingId == null) return;
    const item = findItemById(state.deletingId);
    try {
        await apiDelete(state.deletingId);
        cancelDelete();
        showToast(`${item ? `"${item.nome}"` : 'Item'} foi deletado`, 'warning');
        await loadItems();
    } catch (err) {
        console.error(err);
        showToast(err.message || 'Erro ao deletar', 'error');
    }
}

function switchViewMode(mode) {
    state.viewMode = mode;
    $('#btn-grid').classList.toggle('active', mode === 'grid');
    $('#btn-table').classList.toggle('active', mode === 'table');
    $('#items-grid').hidden = mode !== 'grid';
    $('#items-table').hidden = mode !== 'table';
    render();
}

function filterByCategory(category) {
    state.filterCategory = category;
    $$('.cat-tab').forEach((tab) => tab.classList.toggle('active', tab.dataset.cat === category));
    render();
}

function filterByState(s) {
    state.filterState = state.filterState === s ? null : s;
    $$('.state-pill').forEach((pill) => {
        pill.classList.remove('active-livre', 'active-usado', 'active-quebrado');
        if (state.filterState && pill.dataset.state === state.filterState) {
            pill.classList.add(`active-${state.filterState}`);
        }
    });
    render();
}

function showToast(message, type = 'success') {
    const t = document.createElement('div');
    t.className = `toast ${type}`;
    t.innerHTML = `<span class="toast-dot"></span>${message}`;
    $('#toast-wrap').appendChild(t);
    setTimeout(() => {
        t.style.opacity = '0';
        t.style.transform = 'translateY(6px)';
        t.style.transition = 'opacity .2s, transform .2s';
        setTimeout(() => t.remove(), 220);
    }, 2600);
}

async function loadItems() {
    try {
        const res = await fetch(API_BASE_URL);
        if (!res.ok) throw new Error(`http ${res.status}`);
        state.items = await res.json();
    } catch (err) {
        console.warn('API indisponivel, usando lista vazia.', err);
        state.items = [];
        showToast('Nao foi possivel carregar os itens', 'error');
    }
    render();
}

window.openCreateModal = openCreateModal;
window.openEditModal = openEditModal;
window.openDeleteModal = openDeleteModal;

document.addEventListener('DOMContentLoaded', () => {
    const params = new URLSearchParams(window.location.search);
    const cat = params.get('cat');
    if (cat && VALID_CATEGORIES.includes(cat)) filterByCategory(cat);

    loadItems();

    $('#btn-open-create').addEventListener('click', openCreateModal);
    $('#btn-save').addEventListener('click', saveItem);
    $('#btn-cancel').addEventListener('click', closeFormModal);
    $('#close-modal').addEventListener('click', closeFormModal);
    bindOverlayClose('#form-overlay', closeFormModal);

    $('#confirm-del').addEventListener('click', confirmDelete);
    $('#cancel-del').addEventListener('click', cancelDelete);
    bindOverlayClose('#del-overlay', cancelDelete);

    $('#f-foto').addEventListener('change', handlePhotoUpload);
    $('#btn-grid').addEventListener('click', () => switchViewMode('grid'));
    $('#btn-table').addEventListener('click', () => switchViewMode('table'));
    $$('.cat-tab').forEach((tab) => tab.addEventListener('click', () => filterByCategory(tab.dataset.cat)));
    $$('.state-pill').forEach((pill) => pill.addEventListener('click', () => filterByState(pill.dataset.state)));
    $('#search-input').addEventListener('input', (e) => {
        state.searchQuery = e.target.value;
        render();
    });
});