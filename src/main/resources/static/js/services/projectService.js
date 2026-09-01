import api from './api.js';

export const projectService = {
    
    async listarTodos() {
        return await api.get('/api/projects');
    },

    // buscar projeto específico por nome
    async buscarPorNome(nome) {
        return await api.get('/api/projects/search?nome=' + encodeURIComponent(nome));
    },

    async salvar(objetoProjeto) {
        return await api.post('/api/projects', objetoProjeto);
    },

    // atualizar projeto existente
    async atualizar(id, dadosEditados) {
        return await api.put('/api/projects/' + id, dadosEditados);
    },

    // deletar projeto
    async excluir(id) {
        return await api.delete('/api/projects/' + id);
    }
};