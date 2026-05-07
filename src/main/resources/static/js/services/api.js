const BASE_URL = 'http://localhost:8080';
    
// Essas rotas significam que não necessitam de um token para acesso
const ROTAS_PUBLICAS = [
    '/authentication/login/',
    '/authentication/registro/'
];

async function parseResposta(resposta) {
    const texto = await resposta.text();
    return texto ? JSON.parse(texto) : {};
}

function getHeaders(endpoint, comCorpo = false) {
    const headers = {};

    if (comCorpo) headers['Content-Type'] = 'application/json';

    const ehRotaPublica = ROTAS_PUBLICAS.some(rota => endpoint.startsWith(rota));
    if (!ehRotaPublica) {
        const token = localStorage.getItem('token');
        if (!token) {
            window.location.href = './login.html';
            throw new Error('Token não encontrado. Redirecionando para login.');
        }
        headers['Authorization'] = token;
    }

    return headers;
}

function verificarErroAutorizacao(status) {
    if (status === 401 || status === 403) {
        localStorage.removeItem('token');
        window.location.href = '/login.html';
        throw new Error('Sessão expirada. Redirecionando para login.');
    }
}

const api = {
    async get(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint, {
            headers: getHeaders(endpoint)
        });
        verificarErroAutorizacao(resposta.status);
        if (!resposta.ok) {
            const erro = await parseResposta(resposta);
            throw new Error(erro.message || 'Erro na requisição');
        }
        return await parseResposta(resposta);
    },

    async post(endpoint, corpo) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'POST',
            headers: getHeaders(endpoint, true),
            body: JSON.stringify(corpo)
        });
        verificarErroAutorizacao(resposta.status);
        if (!resposta.ok) {
            const erro = await parseResposta(resposta);
            throw new Error(erro.message || 'Erro na requisição');
        }
        return await parseResposta(resposta);
    },

    async put(endpoint, corpo) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'PUT',
            headers: getHeaders(endpoint, true),
            body: JSON.stringify(corpo)
        });
        verificarErroAutorizacao(resposta.status);
        if (!resposta.ok) {
            const erro = await parseResposta(resposta);
            throw new Error(erro.message || 'Erro na requisição');
        }
        return await parseResposta(resposta);
    },

    async delete(endpoint) {
        const resposta = await fetch(BASE_URL + endpoint, {
            method: 'DELETE',
            headers: getHeaders(endpoint)
        });
        verificarErroAutorizacao(resposta.status);
        if (!resposta.ok) throw new Error('Erro ao deletar');
        return resposta.ok;
    }
};

export default api;