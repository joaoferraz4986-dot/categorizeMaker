package com.makernav.categorize.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.makernav.categorize.dto.ProjetoRequestDTO;
import com.makernav.categorize.dto.ProjetoResponseDTO;
import com.makernav.categorize.infra.repository.ItemRepository;
import com.makernav.categorize.infra.repository.ProjetoItemRepository;
import com.makernav.categorize.infra.repository.ProjetoRepository;
import com.makernav.categorize.infra.repository.UsuarioRepository;
import com.makernav.categorize.model.Item;
import com.makernav.categorize.model.Projeto;
import com.makernav.categorize.model.ProjetoItem;
import com.makernav.categorize.model.TipoEvento;
import com.makernav.categorize.model.Usuario;

@Service
public class ProjetoService {
    private final ProjetoRepository projetoRepository;
    private final ProjetoItemRepository projetoItemRepository;
    private final ItemRepository itemRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoService eventoService;

    public ProjetoService(ProjetoRepository projetoRepository, ProjetoItemRepository projetoItemRepository,
            ItemRepository itemRepository, UsuarioRepository usuarioRepository, EventoService eventoService) {
        this.projetoRepository = projetoRepository;
        this.projetoItemRepository = projetoItemRepository;
        this.itemRepository = itemRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoService = eventoService;
    }

    @Transactional(readOnly = true)
    public List<ProjetoResponseDTO> listarTodos() {
        return projetoRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public Optional<ProjetoResponseDTO> buscarPorId(int id) {
        return projetoRepository.findById(id).map(this::toResponse);
    }

    @Transactional
    public ProjetoResponseDTO salvar(ProjetoRequestDTO dto) {
        Usuario usuario = usuarioAutenticado();
        Projeto projeto = new Projeto();
        aplicarDados(projeto, dto, usuario);
        Projeto salvo = projetoRepository.save(projeto);
        salvarItens(salvo, dto.itens());
        eventoService.registrar(TipoEvento.PROJETO_CRIADO, "PROJETO", salvo.getIdProjeto(), "Projeto criado", salvo.getNomeProjeto(), usuario);
        return toResponse(salvo);
    }

    @Transactional
    public ProjetoResponseDTO atualizar(int id, ProjetoRequestDTO dto) {
        Usuario usuario = usuarioAutenticado();
        Projeto projeto = projetoRepository.findById(id).orElseThrow();
        aplicarDados(projeto, dto, usuario);
        projetoItemRepository.deleteByProjetoIdProjeto(id);
        Projeto salvo = projetoRepository.save(projeto);
        salvarItens(salvo, dto.itens());
        eventoService.registrar(TipoEvento.PROJETO_ATUALIZADO, "PROJETO", salvo.getIdProjeto(), "Projeto atualizado", salvo.getNomeProjeto(), usuario);
        return toResponse(salvo);
    }

    @Transactional
    public void deletar(int id) {
        Usuario usuario = usuarioAutenticado();
        Projeto projeto = projetoRepository.findById(id).orElseThrow();
        eventoService.registrar(TipoEvento.PROJETO_EXCLUIDO, "PROJETO", id, "Projeto excluído", projeto.getNomeProjeto(), usuario);
        projetoRepository.delete(projeto);
    }

    private void aplicarDados(Projeto projeto, ProjetoRequestDTO dto, Usuario usuario) {
        projeto.setNome(dto.nome().trim());
        projeto.setCategoria(dto.categoria());
        projeto.setDescricao(dto.descricao().trim());
        projeto.setImagem(dto.imagem());
        projeto.setUsuario(usuario);
        if (projeto.getDataInicio() == null) {
            projeto.setDataInicio(new Date());
        }
    }

    private void salvarItens(Projeto projeto, List<ProjetoRequestDTO.ItemProjetoRequest> itens) {
        if (itens == null) {
            return;
        }
        for (ProjetoRequestDTO.ItemProjetoRequest dto : itens) {
            Item item = itemRepository.findById(dto.idItem()).orElseThrow();
            if (dto.quantidadeUsada() > item.getQuantidade()) {
                throw new IllegalArgumentException("A quantidade alocada excede a quantidade disponível para o item " + item.getNome());
            }
            ProjetoItem projetoItem = new ProjetoItem();
            projetoItem.setProjeto(projeto);
            projetoItem.setItem(item);
            projetoItem.setQuantidadeUsada(dto.quantidadeUsada());
            projetoItem.setDataAlocacao(new Date());
            projetoItemRepository.save(projetoItem);
        }
    }

    private ProjetoResponseDTO toResponse(Projeto projeto) {
        List<ProjetoResponseDTO.ItemProjetoResponse> itens = projetoItemRepository.findByProjetoIdProjeto(projeto.getIdProjeto()).stream()
                .map(item -> new ProjetoResponseDTO.ItemProjetoResponse(item.getItem().getIdItem(), item.getItem().getNome(), item.getQuantidadeUsada()))
                .toList();
        return new ProjetoResponseDTO(projeto.getIdProjeto(), projeto.getNome(), projeto.getCategoria(), projeto.getDescricao(), projeto.getImagem(), itens);
    }

    private Usuario usuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Usuario usuario)) {
            throw new IllegalStateException("Usuário autenticado não encontrado");
        }
        return usuarioRepository.findById(usuario.getIdUsuario()).orElseThrow();
    }
}
