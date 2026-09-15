const PAGINAS_PROTEGIDAS = new Set(['/projetos.html', '/dashboard.html']);

function obterToken() {
    return localStorage.getItem('token');
}

function redirecionarParaLogin() {
    window.location.replace('./login.html');
}

async function navegarComAutenticacao(url) {
    const token = obterToken();

    if (!token) {
        redirecionarParaLogin();
        return;
    }

    const resposta = await fetch(url.href, {
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

    if (resposta.status === 401 || resposta.status === 403) {
        localStorage.removeItem('token');
        redirecionarParaLogin();
        return;
    }

    if (!resposta.ok) {
        throw new Error(`Não foi possível abrir a página (${resposta.status})`);
    }

    const html = await resposta.text();
    window.history.pushState({}, '', url.href);
    document.open();
    document.write(html);
    document.close();
}

document.addEventListener('click', (evento) => {
    const link = evento.target.closest('a[href]');

    if (!link) {
        return;
    }

    const url = new URL(link.href, window.location.href);

    if (url.origin !== window.location.origin || !PAGINAS_PROTEGIDAS.has(url.pathname)) {
        return;
    }

    evento.preventDefault();
    navegarComAutenticacao(url).catch(() => redirecionarParaLogin());
});

window.addEventListener('popstate', () => {
    const url = new URL(window.location.href);

    if (PAGINAS_PROTEGIDAS.has(url.pathname)) {
        navegarComAutenticacao(url).catch(() => redirecionarParaLogin());
    }
});
