# 🧪 Guia de Testes com JUnit 5 - Projeto Zero Hunger

## 📋 Índice
1. [Configuração](#configuração)
2. [Estrutura de Testes](#estrutura-de-testes)
3. [Tipos de Testes](#tipos-de-testes)
4. [Executando Testes](#executando-testes)
5. [Exemplos Práticos](#exemplos-práticos)
6. [Boas Práticas](#boas-práticas)

## ⚙️ Configuração

### Dependências no `build.gradle`
```gradle
dependencies {
    // JUnit 5 (já incluído no spring-boot-starter-test)
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
    testImplementation 'org.springframework.security:spring-security-test'
    testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
}

tasks.named('test') {
    useJUnitPlatform()
}
```

### Configuração de Teste (`application-test.properties`)
```properties
# H2 Database para testes
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA configurações
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
```

## 📁 Estrutura de Testes

```
src/test/java/
├── com/zeroHunger/demo/
│   ├── controller/          # Testes de integração para controllers
│   ├── service/             # Testes unitários para services
│   ├── repository/          # Testes de repository com H2
│   └── GuiaJUnitTest.java   # Guia completo de exemplos
```

## 🔍 Tipos de Testes

### 1. Testes Unitários (Services)
```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;
    
    @InjectMocks
    private UsuarioService usuarioService;
    
    @Test
    void deveSalvarUsuarioComSucesso() {
        // Given
        Usuario usuario = new Usuario();
        when(usuarioRepository.save(any())).thenReturn(usuario);
        
        // When
        Usuario resultado = usuarioService.salvarUsuario(usuario);
        
        // Then
        assertNotNull(resultado);
        verify(usuarioRepository).save(usuario);
    }
}
```

### 2. Testes de Integração (Controllers)
```java
@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UsuarioService usuarioService;
    
    @Test
    @WithMockUser
    void deveCriarUsuarioComSucesso() throws Exception {
        mockMvc.perform(post("/api/usuarios/criar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isOk());
    }
}
```

### 3. Testes de Repository
```java
@DataJpaTest
@ActiveProfiles("test")
class UsuarioRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Test
    void deveEncontrarUsuarioPorEmail() {
        // Given
        Usuario usuario = new Usuario();
        entityManager.persistAndFlush(usuario);
        
        // When
        Optional<Usuario> resultado = usuarioRepository.findByEmail("teste@email.com");
        
        // Then
        assertTrue(resultado.isPresent());
    }
}
```

## 🚀 Executando Testes

### Comandos Gradle

```bash
# Executar todos os testes
./gradlew test

# Executar testes específicos
./gradlew test --tests "*UsuarioServiceTest"

# Executar com informações detalhadas
./gradlew test --info

# Executar testes de uma classe específica
./gradlew test --tests "com.zeroHunger.demo.service.UsuarioServiceTest"

# Executar um método específico
./gradlew test --tests "*.deveSalvarUsuarioComSucesso"
```

### Relatórios de Teste
Após executar os testes, o relatório HTML estará disponível em:
```
build/reports/tests/test/index.html
```

## 💡 Exemplos Práticos

### Testando Exceções
```java
@Test
void deveLancarExcecaoQuandoEmailJaExiste() {
    // Given
    when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);
    
    // When & Then
    RuntimeException exception = assertThrows(RuntimeException.class, 
        () -> usuarioService.salvarUsuario(usuario));
    
    assertEquals("Email ja cadastrado!", exception.getMessage());
}
```

### Testando com ArgumentCaptor
```java
@Test
void deveCapturarArgumentosDoMock() {
    // Given
    ArgumentCaptor<Usuario> usuarioCaptor = ArgumentCaptor.forClass(Usuario.class);
    
    // When
    usuarioService.salvarUsuario(usuario);
    
    // Then
    verify(usuarioRepository).save(usuarioCaptor.capture());
    Usuario usuarioCapturado = usuarioCaptor.getValue();
    assertEquals("teste@email.com", usuarioCapturado.getEmail());
}
```

### Testes Parametrizados
```java
@ParameterizedTest
@ValueSource(strings = {"admin@test.com", "user@test.com", "fornecedor@test.com"})
void deveValidarEmailsValidos(String email) {
    assertTrue(email.contains("@"));
    assertTrue(email.contains(".com"));
}

@ParameterizedTest
@CsvSource({
    "1, João, COMPRADOR",
    "2, Maria, FORNECEDOR", 
    "3, Pedro, ADMIN"
})
void deveValidarTiposDeUsuario(Long id, String nome, String tipo) {
    assertNotNull(id);
    assertNotNull(nome);
    assertTrue(tipo.matches("COMPRADOR|FORNECEDOR|ADMIN"));
}
```

### Testes com Timeout
```java
@Test
@Timeout(2) // 2 segundos
void deveExecutarRapidamente() {
    // Teste que deve executar em menos de 2 segundos
    usuarioService.buscarUsuarioPorId(1L);
}
```

## 📝 Principais Annotations

### JUnit 5
- `@Test` - Marca um método como teste
- `@DisplayName` - Nome descritivo para o teste
- `@BeforeEach` - Executado antes de cada teste
- `@AfterEach` - Executado após cada teste
- `@BeforeAll` - Executado uma vez antes de todos os testes
- `@AfterAll` - Executado uma vez após todos os testes
- `@ParameterizedTest` - Teste parametrizado
- `@Nested` - Agrupa testes relacionados
- `@Disabled` - Desabilita um teste
- `@Timeout` - Define timeout para o teste

### Spring Boot Test
- `@SpringBootTest` - Carrega contexto completo da aplicação
- `@WebMvcTest` - Testa apenas a camada web
- `@DataJpaTest` - Testa apenas a camada de persistência
- `@MockBean` - Cria mock de um bean Spring
- `@WithMockUser` - Simula usuário autenticado

### Mockito
- `@Mock` - Cria um mock
- `@InjectMocks` - Injeta mocks em uma instância
- `@ExtendWith(MockitoExtension.class)` - Habilita Mockito

## 🎯 Principais Assertions

```java
// Igualdade
assertEquals(expected, actual)
assertNotEquals(expected, actual)

// Valores booleanos
assertTrue(condition)
assertFalse(condition)

// Nulos
assertNull(object)
assertNotNull(object)

// Exceções
assertThrows(Exception.class, executable)

// Múltiplas assertions
assertAll("descrição",
    () -> assertEquals(expected1, actual1),
    () -> assertTrue(condition),
    () -> assertNotNull(object)
)
```

## 🔧 Principais Métodos do Mockito

```java
// Definir comportamento
when(mock.method()).thenReturn(value)
when(mock.method()).thenThrow(Exception.class)

// Verificar chamadas
verify(mock).method()
verify(mock, times(n)).method()
verify(mock, never()).method()
verify(mock, atLeast(n)).method()

// Matchers de argumentos
anyString(), anyInt(), any()
eq(value)
argThat(matcher)

// Capturar argumentos
ArgumentCaptor<Type> captor = ArgumentCaptor.forClass(Type.class)
verify(mock).method(captor.capture())
Type capturedValue = captor.getValue()
```

## ✅ Boas Práticas

### 1. Estrutura AAA (Arrange, Act, Assert)
```java
@Test
void exemploEstruturAAA() {
    // Arrange (Given) - Preparar dados e mocks
    Usuario usuario = new Usuario();
    when(repository.save(any())).thenReturn(usuario);
    
    // Act (When) - Executar a ação
    Usuario resultado = service.salvarUsuario(usuario);
    
    // Assert (Then) - Verificar resultado
    assertNotNull(resultado);
    verify(repository).save(usuario);
}
```

### 2. Nomes Descritivos
```java
// ❌ Ruim
@Test
void test1() { }

// ✅ Bom
@Test
@DisplayName("Deve salvar usuário com sucesso quando email não existe")
void deveSalvarUsuarioComSucessoQuandoEmailNaoExiste() { }
```

### 3. Um Conceito por Teste
```java
// ❌ Ruim - testa múltiplos conceitos
@Test
void testeUsuario() {
    // Testa criação E busca E atualização
}

// ✅ Bom - um conceito por teste
@Test
void deveCriarUsuario() { }

@Test
void deveBuscarUsuario() { }

@Test
void deveAtualizarUsuario() { }
```

### 4. Usar @DisplayName para Clareza
```java
@DisplayName("Testes de Autenticação de Usuário")
class UsuarioAuthTest {
    
    @Test
    @DisplayName("Deve autenticar usuário com credenciais válidas")
    void deveAutenticarComCredenciaisValidas() { }
    
    @Test
    @DisplayName("Deve falhar na autenticação com senha inválida")
    void deveFalharComSenhaInvalida() { }
}
```

### 5. Configurar Dados de Teste no @BeforeEach
```java
@BeforeEach
void setUp() {
    usuario = new Usuario();
    usuario.setEmail("teste@email.com");
    usuario.setNome("Teste Usuario");
    // Configurar dados comuns a todos os testes
}
```

## 🐛 Debugging de Testes

### Executar Testes com Debug
```bash
# Executar com logs detalhados
./gradlew test --debug

# Executar teste específico com info
./gradlew test --tests "*UsuarioServiceTest.deveSalvarUsuario*" --info
```

### Verificar Relatório de Falhas
O relatório HTML mostra:
- Quais testes falharam
- Stack traces completos
- Tempo de execução
- Cobertura de código

## 📊 Exemplo Completo

Veja o arquivo `GuiaJUnitTest.java` para um exemplo completo com todas as funcionalidades demonstradas.

---

**💡 Dica:** Execute `./gradlew test --tests "*GuiaJUnitTest"` para ver todos os exemplos em ação!
