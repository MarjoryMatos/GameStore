package com.generation.gamestore.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "tb_categoria")
public class Categoria {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private long id;
		
		@NotNull
		@Size(min = 1, max = 300)
		private String categoria;
		
		@NotNull
		@Size(min = 1, max = 500)
		private String plataforma;
		
		@NotNull
		@Size(min = 1, max = 500)
		private String console;
		
		@OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
		@JsonIgnoreProperties("categoria")
		private List<Produto> produto;

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getCategoria() {
			return categoria;
		}

		public void setCategoria(String categoria) {
			this.categoria = categoria;
		}

		public String getPlataforma() {
			return plataforma;
		}

		public void setPlataforma(String plataforma) {
			this.plataforma = plataforma;
		}

		public String getConsole() {
			return console;
		}

		public void setConsole(String console) {
			this.console = console;
		}
		
		

}
