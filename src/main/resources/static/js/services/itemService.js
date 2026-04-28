import api from './api.js';

export const itemService = {
    
    // listar todas as peças
    async listarTodos() {
        return await api.get('/items');
    },


    async buscarPorNome(nome) {
        return await api.get('/items/search?nome=' + nome);
    },

    // post | objetoItem = json
    async salvar(objetoItem) {
        return await api.post('/items', objetoItem);
    },

    // pede id pra saber qual item vai mudar
    async atualizar(id, dadosEditados) {
        return await api.put('/items/' + id, dadosEditados);
    },

    // deleta by id
    async excluir(id) {
        return await api.delete('/items/' + id);
    }
};