import api from './api.js';

export const projectService = {
    
    async listarTodos() {
        return await api.get('/projeto');
    },

    // buscar projeto específico por nome
    async buscarPorNome(nome) {
        return await api.get('/projeto/search?nome=' + encodeURIComponent(nome));
    },

    async salvar(objetoProjeto) {
        return await api.post('/projeto', objetoProjeto);
    },

    // atualizar projeto existente
    async atualizar(id, dadosEditados) {
        return await api.put('/projeto/' + id, dadosEditados);
    },

    // deletar projeto
    async excluir(id) {
        return await api.delete('/projeto/' + id);
    }
};
