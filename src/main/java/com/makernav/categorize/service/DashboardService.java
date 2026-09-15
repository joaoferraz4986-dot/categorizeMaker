package com.makernav.categorize.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.makernav.categorize.dto.DashboardResponse;
import com.makernav.categorize.infra.repository.EventoRepository;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.infra.repository.ProjetoRepository;
import com.makernav.categorize.model.Estado;
import com.makernav.categorize.model.Evento;
import com.makernav.categorize.model.Projeto;

@Service
public class DashboardService {
    private static final Locale LOCALE = Locale.forLanguageTag("pt-BR");
    private static final DateTimeFormatter KEY_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM");
    private final ProjetoRepository projetoRepository;
    private final ItemRepository itemRepository;
    private final EventoRepository eventoRepository;

    public DashboardService(ProjetoRepository projetoRepository, ItemRepository itemRepository, EventoRepository eventoRepository) {
        this.projetoRepository = projetoRepository;
        this.itemRepository = itemRepository;
        this.eventoRepository = eventoRepository;
    }

    public DashboardResponse carregar() {
        LocalDate hoje = LocalDate.now();
        YearMonth mesAtual = YearMonth.from(hoje);
        YearMonth mesAnterior = mesAtual.minusMonths(1);
        List<Projeto> projetos = projetoRepository.findAll();
        List<YearMonth> ultimosMeses = java.util.stream.IntStream.rangeClosed(0, 5)
                .mapToObj(mesAtual::minusMonths)
                .toList()
                .reversed();
        Map<YearMonth, Long> criadosPorMes = projetos.stream()
                .filter(projeto -> projeto.getDataInicio() != null)
                .collect(Collectors.groupingBy(
                        projeto -> YearMonth.from(projeto.getDataInicio().toInstant().atZone(ZoneId.systemDefault())),
                        Collectors.counting()));
        var itens = itemRepository.findAll();
        var snapshotAtual = snapshotAtual(itens);

        List<DashboardResponse.MonthSummary> meses = ultimosMeses.stream()
                .map(mes -> new Object[] { mes, snapshotDoMes(mes, snapshotAtual) })
                .map(mes -> new DashboardResponse.MonthSummary(
                        nomeMes((YearMonth) mes[0]),
                        ((YearMonth) mes[0]).format(KEY_FORMAT),
                        criadosPorMes.getOrDefault((YearMonth) mes[0], 0L).intValue(),
                        (int) projetos.stream().filter(projeto -> projeto.getDataInicio() != null
                                && !YearMonth.from(projeto.getDataInicio().toInstant().atZone(ZoneId.systemDefault())).isAfter((YearMonth) mes[0])).count(),
                        ((DashboardResponse.ItemSummary) mes[1]).totalQuantity(),
                        ((DashboardResponse.ItemSummary) mes[1]).availableQuantity(),
                        ((DashboardResponse.ItemSummary) mes[1]).usedQuantity(),
                        ((DashboardResponse.ItemSummary) mes[1]).brokenQuantity()))
                .toList();

        List<DashboardResponse.ActiveProject> projetosAtivos = projetos.stream()
                .filter(projeto -> projeto.getDataFim() == null)
                .sorted(Comparator.comparing(Projeto::getDataInicio, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(6)
                .map(projeto -> new DashboardResponse.ActiveProject(
                        projeto.getNomeProjeto(),
                        projeto.getCategoria().name(),
                        projeto.getDescricao(),
                        data(projeto.getDataInicio())))
                .toList();

        List<DashboardResponse.Event> eventos = eventoRepository.findTop8ByOrderByDataEventoDesc().stream()
                .map(this::evento)
                .toList();

        int total = itens.stream().mapToInt(item -> item.getQuantidade()).sum();
        int disponiveis = itens.stream().filter(item -> item.getEstado() == Estado.LIVRE)
                .mapToInt(item -> item.getQuantidade()).sum();
        int emUso = itens.stream().filter(item -> item.getEstado() == Estado.USADO)
                .mapToInt(item -> item.getQuantidade()).sum();
        int quebrados = itens.stream().filter(item -> item.getEstado() == Estado.QUEBRADO)
                .mapToInt(item -> item.getQuantidade()).sum();

        return new DashboardResponse(
                nomeMes(mesAtual),
                resumo(meses, mesAtual),
                resumo(meses, mesAnterior),
                meses,
                projetosAtivos,
                eventos,
                new DashboardResponse.ItemSummary(total, disponiveis, emUso, quebrados));
    }

    private DashboardResponse.MonthSummary resumo(List<DashboardResponse.MonthSummary> meses, YearMonth mes) {
        return meses.stream()
                .filter(item -> item.key().equals(mes.format(KEY_FORMAT)))
                .findFirst()
                .orElse(new DashboardResponse.MonthSummary(nomeMes(mes), mes.format(KEY_FORMAT), 0, 0, 0, 0, 0, 0));
    }

    private String nomeMes(YearMonth mes) {
        return mes.getMonth().getDisplayName(TextStyle.FULL, LOCALE).replace("-feira", "");
    }

    private LocalDate data(java.util.Date data) {
        return data == null ? null : data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private DashboardResponse.Event evento(Evento evento) {
        return new DashboardResponse.Event(
                evento.getTipo().name().toLowerCase(),
                evento.getTitulo(),
                evento.getDescricao(),
                data(evento.getDataEvento()));
    }

    private DashboardResponse.ItemSummary snapshotAtual(List<com.makernav.categorize.model.Item> itens) {
        return new DashboardResponse.ItemSummary(
                itens.stream().mapToInt(item -> item.getQuantidade()).sum(),
                itens.stream().filter(item -> item.getEstado() == Estado.LIVRE).mapToInt(item -> item.getQuantidade()).sum(),
                itens.stream().filter(item -> item.getEstado() == Estado.USADO).mapToInt(item -> item.getQuantidade()).sum(),
                itens.stream().filter(item -> item.getEstado() == Estado.QUEBRADO).mapToInt(item -> item.getQuantidade()).sum());
    }

    private DashboardResponse.ItemSummary snapshotDoMes(YearMonth mes, DashboardResponse.ItemSummary atual) {
        return eventoRepository.findAllByOrderByDataEventoDesc().stream()
                .filter(evento -> evento.getDataEvento() != null
                        && !evento.getDataEvento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(mes.atEndOfMonth()))
                .filter(evento -> evento.getTotalQuantidade() != null)
                .findFirst()
                .map(evento -> new DashboardResponse.ItemSummary(
                        evento.getTotalQuantidade(),
                        evento.getQuantidadeLivre(),
                        evento.getQuantidadeUsado(),
                        evento.getQuantidadeQuebrado()))
                .orElse(atual);
    }
}
