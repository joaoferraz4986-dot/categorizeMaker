const BASE_URL = '';
import { logout } from './authGuard.js';

const ROTAS_PUBLICAS = [
    '/authentication/login/',
    '/authentication/registro/'
];

async function parseResposta(resposta) {
    const texto = await resposta.text();
    if (!texto) return {};

    try {
        return JSON.parse(texto);
    } catch {
        return { message: texto };
    }
}

function getHeaders(endpoint, comCorpo = false) {
    const headers = {};
    if (comCorpo) headers['Content-Type'] = 'application/json';

    const ehRotaPublica = ROTAS_PUBLICAS.some((rota) => endpoint.startsWith(rota));
    if (!ehRotaPublica) {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = './login.html';
            throw new Error('Token não encontrado. Redirecionando para login.');
        }
        headers.Authorization = token;
    }

    return headers;
}

function verificarErroAutorizacao(status) {
    if (status === 401 || status === 403) {
        logout();
        throw new Error('Sessão expirada. Redirecionando para login.');
    }
}

async function verificarResposta(resposta) {
    verificarErroAutorizacao(resposta.status);
    if (!resposta.ok) {
        const contentType = resposta.headers.get('content-type') || '';
        if (contentType.includes('application/json')) {
            const erro = await parseResposta(resposta);
            throw new Error(erro.message || 'Erro na requisição');
        }
        throw new Error(`Não foi possível concluir a requisição (${resposta.status})`);
    }
}

const api = {
    async get(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint, { headers: getHeaders(endpoint) });
        await verificarResposta(resposta);
        return parseResposta(resposta);
    },

    async getBlob(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint, { headers: getHeaders(endpoint) });
        await verificarResposta(resposta);
        return resposta.blob();
    },

    async post(endpoint, corpo) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'POST',
            headers: getHeaders(endpoint, true),
            body: JSON.stringify(corpo)
        });
        await verificarResposta(resposta);
        return parseResposta(resposta);
    },

    async put(endpoint, corpo) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'PUT',
            headers: getHeaders(endpoint, true),
            body: JSON.stringify(corpo)
        });
        await verificarResposta(resposta);
        return parseResposta(resposta);
    },

    async delete(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'DELETE',
            headers: getHeaders(endpoint)
        });
        await verificarResposta(resposta);
        return true;
    }
};

export default api;
