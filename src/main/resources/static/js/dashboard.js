import api from './services/api.js';

const ICONS = {
    projects: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4 6.5A2.5 2.5 0 0 1 6.5 4H10l2 2h5.5A2.5 2.5 0 0 1 20 8.5v8A2.5 2.5 0 0 1 17.5 19h-11A2.5 2.5 0 0 1 4 16.5z"/></svg>',
    archive: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M3 7h18"/><path d="M5 7v13h14V7"/><path d="M4 4h16v3H4z"/><path d="M9 11h6"/></svg>',
    hammer: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="m14 6 4 4"/><path d="m13 7 4-4 4 4-4 4"/><path d="m3 21 10-10"/><path d="m7 17 3 3"/></svg>',
    broken: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="m14 6 4 4"/><path d="m13 7 4-4 4 4-4 4"/><path d="m3 21 10-10"/><path d="m7 17 3 3"/><path d="m4 4 16 16"/></svg>',
    chart: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M4 19V5"/><path d="M4 19h16"/><path d="m7 15 3-4 3 2 5-7"/></svg>',
    activity: '<svg viewBox="0 0 24 24" aria-hidden="true"><path d="M3 12h4l3 8 4-16 3 8h4"/></svg>',
    calendar: '<svg viewBox="0 0 24 24" aria-hidden="true"><rect x="3" y="4" width="18" height="17" rx="2"/><path d="M16 2v4M8 2v4M3 10h18"/></svg>'
};

const monthLabel = (value) => value.charAt(0).toUpperCase() + value.slice(1);
const formatDate = (value) => value ? new Intl.DateTimeFormat('pt-BR').format(new Date(`${value}T00:00:00`)) : 'Sem data';
const escapeHtml = (value) => String(value ?? '').replace(/[&<>"']/g, (char) => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[char]));

function metric(icon, title, description, value) {
    return `<li class="listaItem"><div class="listaItemImagem">${ICONS[icon]}</div><span class="listaItem-texto"><span class="listaItemTitulo">${title}</span><span class="listaItemSubtexto">${description}</span></span><span class="listaItemQuantidade">${value}</span></li>`;
}

function renderMonthSection(element, summary) {
    element.querySelector('[data-month-title]').textContent = monthLabel(summary.label);
    element.querySelector('[data-month-list]').innerHTML = [
        metric('projects', 'Projetos', 'Projetos criados no mês', summary.projectsCreated),
        metric('archive', 'Projetos acumulados', 'Projetos registrados até o mês', summary.projectsTotal),
        metric('hammer', 'Itens armazenados', 'Quantidade total cadastrada', summary.totalQuantity),
        metric('broken', 'Itens quebrados', 'Quantidade indisponível', summary.brokenQuantity),
        metric('activity', 'Itens em uso', 'Quantidade alocada', summary.usedQuantity)
    ].join('');
}

function renderChart(months) {
    const chart = document.querySelector('#monthly-chart');
    const max = Math.max(...months.map((month) => month.projectsCreated), 1);
    chart.innerHTML = months.map((month) => `<div class="chart-column"><span class="chart-value">${month.projectsCreated}</span><div class="chart-bar" style="height: ${Math.max(8, (month.projectsCreated / max) * 100)}%"></div><span class="chart-label">${month.label.slice(0, 3)}</span></div>`).join('');
}

function renderActiveProjects(projects) {
    const list = document.querySelector('#active-projects');
    list.innerHTML = projects.length ? projects.map((project) => `<li class="projetosItens"><div class="item"><div class="projetoDescricao"><span class="projetoNome">${escapeHtml(project.name)}</span><span class="projetoContador">${escapeHtml(project.category)}</span></div><p class="projetoDetalhe">${escapeHtml(project.description)}</p><div class="progresso"><span style="width: 100%"></span></div><small class="projetoData">Iniciado em ${formatDate(project.startDate)}</small></div></li>`).join('') : '<li class="dashboard-empty">Nenhum projeto ativo.</li>';
}

function renderEvents(events) {
    const list = document.querySelector('#event-list');
    list.innerHTML = events.length ? events.map((event) => `<li class="logConteudo"><div class="logImagem">${ICONS.projects}</div><span class="logItemTexto"><strong>${escapeHtml(event.title)}</strong><small>${escapeHtml(event.description)} · ${formatDate(event.date)}</small></span></li>`).join('') : '<li class="dashboard-empty">Nenhum evento registrado.</li>';
}

function bindNavigation() {
    document.querySelectorAll('.navLink').forEach((link) => link.addEventListener('click', () => {
        document.querySelectorAll('.navLink').forEach((item) => item.classList.remove('active'));
        link.classList.add('active');
        const page = document.querySelector(`.pagina[data-pagina="${link.dataset.cat}"]`);
        if (page) {
            document.querySelectorAll('.pagina').forEach((item) => { item.hidden = true; });
            page.hidden = false;
        }
    }));
}

async function loadDashboard() {
    const dashboard = await api.get('/api/dashboard');
    document.querySelector('[data-current-date]').textContent = `${monthLabel(dashboard.currentMonth)} · resumo atualizado hoje`;
    renderMonthSection(document.querySelector('#current-section'), dashboard.current);
    renderMonthSection(document.querySelector('#previous-section'), dashboard.previous);
    renderChart(dashboard.months);
    renderActiveProjects(dashboard.activeProjects);
    renderEvents(dashboard.events);
}

bindNavigation();
loadDashboard().catch(() => {
    document.querySelector('#active-projects').innerHTML = '<li class="dashboard-empty">Não foi possível carregar os dados.</li>';
    document.querySelector('#event-list').innerHTML = '<li class="dashboard-empty">Não foi possível carregar os eventos.</li>';
});
