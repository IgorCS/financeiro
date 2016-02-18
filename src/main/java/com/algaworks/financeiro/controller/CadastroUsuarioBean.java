package com.algaworks.financeiro.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.financeiro.model.Usuario;
import com.algaworks.financeiro.repository.Usuarios;
import com.algaworks.financeiro.service.CadastroUsuarios;
import com.algaworks.financeiro.service.NegocioException;

@Named
@javax.faces.view.ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String nome;
	private String usuarioLogin;

	public String getUsuarioLogin() {
		return usuarioLogin;
	}

	public void setUsuarioLogin(String usuarioLogin) {
		this.usuarioLogin = this.nome;
	}

	@Inject
	private CadastroUsuarios cadastroUsuario;

	@Inject
	private Usuario usuario;

	private List<Usuario> listaUsuario;

	@Inject
	private Usuarios usuarios;

	private List<String> usuarioExiste;

	public List<String> getUsuarioExiste() {
		return this.usuarioExiste;
	}

	public void prepararCadastroUsuario() {

		if (this.usuario == null) {
			this.usuario = new Usuario();
		}
	}

	public List<String> usuariosExistentes(String nome) {
		return this.usuarios.usuarioExiste(nome);
	}

	public void verificarDisponibilidadeLogin() {
		FacesMessage msg = null;
		// simula demora no processamento
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		
		/*
		 HashMap<Integer, List<String>> clientes = new HashMap<Integer, List<String>> ();  
       List<String> nomes = new ArrayList<String>();
		 */

		Map<Integer, String> usuariosExistentes = new HashMap<Integer, String>();
		List<String> nomesExistentes = new ArrayList<String>();
		
		nomesExistentes.addAll(usuarios.usuarioExiste(usuarioLogin));
		
		this.usuarioExiste = this.usuarios.usuarioExiste(usuarioLogin);
		
		usuariosExistentes.put(new Integer(1), nomesExistentes.toString());
		nomesExistentes = new ArrayList<String>();

		for (Integer codigo : usuariosExistentes.keySet()) {
			 // for (String nome : clientes.get(codigo)) {
			for (String nomeUsuario : usuariosExistentes.values()) {				
				System.out.println("Código: "          + codigo +   
	        	                   " - Cliente: "      + usuarios.usuarioExiste(usuarioLogin)
	        	                   +"ARRAY DE NOMES DE USUARIOS:"+nomeUsuario);
				System.out.println("KEYTOSEARCH: " + usuarios.usuarioExiste(usuarioLogin) 
				        + " USUARIO DIGITADO: "
						+ this.usuario.getNome());

			}
		}

		if (usuarioExiste.contains(this.usuario.getNome())) {
			// if ("joao".equals(this.usuario.getNome())){
			// if ("joao".equalsIgnoreCase(this.login)) {
			msg = new FacesMessage("Este Login já está em uso!");
			msg.setSeverity(FacesMessage.SEVERITY_WARN);
		} else {
			msg = new FacesMessage("Login disponível.");
		}
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void salvarUsuario() {
		FacesContext context = FacesContext.getCurrentInstance();

		try {
			this.cadastroUsuario.salvarUsuario(this.usuario);

			this.usuario = new Usuario();
			context.addMessage(null, new FacesMessage("Você foi cadastrado cadastrado(a) com sucesso!"));
		} catch (NegocioException e) {

			FacesMessage mensagem = new FacesMessage(e.getMessage());
			mensagem.setSeverity(FacesMessage.SEVERITY_ERROR);
			context.addMessage(null, mensagem);
		}
	}

	public List<Usuario> getTodosUsuarios() {
		return this.listaUsuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
