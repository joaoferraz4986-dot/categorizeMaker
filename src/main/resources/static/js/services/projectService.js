import api from './api.js';

export const projectService = {
    
    // listar todos os projetos
    async listarTodos() {
        return await api.get('/projects');
    },

    // buscar projeto específico por nome
    async buscarPorNome(nome) {
        return await api.get('/projects/search?nome=' + nome);
    },

    // criar novo projeto | payload = { name, category, description }
    async salvar(objetoProjeto) {
        return await api.post('/projects', objetoProjeto);
    },

    // atualizar projeto existente
    async atualizar(id, dadosEditados) {
        return await api.put('/projects/' + id, dadosEditados);
    },

    // deletar projeto
    async excluir(id) {
        return await api.delete('/projects/' + id);
    }
};