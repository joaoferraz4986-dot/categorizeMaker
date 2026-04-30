import api from './api.js';

export const authService = {

    async cadastrar(dadosUsuario) {
        return await api.post('/authentication/registro/', dadosUsuario);
    },

    async login(credenciais) {
        return await api.post('/authentication/login/', credenciais);
    }

};