import api from './api.js';

function queryString(params = {}) {
    const search = new URLSearchParams();
    Object.entries(params).forEach(([key, value]) => {
        if (value !== null && value !== undefined && String(value).trim() !== '') {
            search.set(key, value);
        }
    });
    const query = search.toString();
    return query ? `?${query}` : '';
}

function normalizePage(response) {
    if (Array.isArray(response)) {
        return { items: response, total: response.length };
    }

    return {
        items: Array.isArray(response?.content) ? response.content : [],
        total: Number(response?.totalElements ?? response?.content?.length ?? 0)
    };
}

export const itemService = {
    async listarTodos(filtros = {}) {
        const response = await api.get(`/api/items${queryString({ size: 100, ...filtros })}`);
        return normalizePage(response);
    },

    async buscarPorNome(nome) {
        return this.listarTodos({ name: nome });
    },

    async salvar(objetoItem) {
        return api.post('/api/items', objetoItem);
    },

    async atualizar(id, dadosEditados) {
        return api.put(`/api/items/${id}`, dadosEditados);
    },

    async excluir(id) {
        return api.delete(`/api/items/${id}`);
    },

    async exportarPdf(filtros = {}) {
        return api.getBlob(`/api/items/export/pdf${queryString(filtros)}`);
    }
};
