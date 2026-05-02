const BASE_URL = 'http://localhost:8080';

async function parseResposta(resposta) {
    const texto = await resposta.text();
    return texto ? JSON.parse(texto) : {};
}

const api = {
    async get(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint);

        if (!resposta.ok) {
            const erro = await parseResposta(resposta);
            throw new Error(erro.message || 'Erro na requisição');
        }

        return await parseResposta(resposta);
    },

    async post(endpoint, corpo) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(corpo)
        });

        if (!resposta.ok) {
            const erro = await parseResposta(resposta);
            throw new Error(erro.message || 'Erro na requisição');
        }

        return await parseResposta(resposta);
    },

    async put(endpoint, corpo) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(corpo)
        });

        if (!resposta.ok) {
            const erro = await parseResposta(resposta);
            throw new Error(erro.message || 'Erro na requisição');
        }

        return await parseResposta(resposta);
    },

    async delete(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'DELETE'
        });

        if (!resposta.ok) {
            throw new Error('Erro ao deletar');
        }

        return resposta.ok;
    }
};

export default api;