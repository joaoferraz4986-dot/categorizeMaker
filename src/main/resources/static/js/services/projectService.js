import api from './api.js';

export const projectService = {
    
    async listarTodos() {
        return await api.get('/projeto/ativos');
    },

    async buscarPorNome(nome) {
        return await api.get('/projeto/search?nome=' + encodeURIComponent(nome));
    },

    async salvar(objetoProjeto) {
        return await api.post('/projeto', objetoProjeto);
    },

    async atualizar(id, dadosEditados) {
        return await api.put('/projeto/' + id, dadosEditados);
    },

    async excluir(id) {
        return await api.delete('/projeto/' + id);
    }
};
