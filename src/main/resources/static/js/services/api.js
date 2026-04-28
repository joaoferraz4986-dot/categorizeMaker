const BASE_URL = 'http://localhost:8080/api';

const api = {
    async get(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint);
        const dados = await resposta.json();
        return dados;
    },

    async post(endpoint, corpo) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(corpo)
        });
        return await resposta.json();
    },

    async put(endpoint, corpo) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(corpo)
        });
        return await resposta.json();
    },

    async delete(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'DELETE'
        });
        // no delete normalmente não é retornado nada então só dar ok
        return resposta.ok;
    }
};

export default api;