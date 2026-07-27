import api from './api.js';
import {logout} from './authGuard.js';

export const authService = {
    async cadastrar(dadosUsuario) {
        return await api.post('/authentication/registro/', dadosUsuario);
    },

    async login(credenciais) {
        return await api.post('/authentication/login/', credenciais);
    },

    logout

};