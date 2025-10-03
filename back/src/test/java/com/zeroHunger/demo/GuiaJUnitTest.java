package com.zeroHunger.demo;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * GUIA COMPLETO DE JUNIT 5 PARA O PROJETO ZERO HUNGER
 * 
 * Este arquivo demonstra as principais funcionalidades do JUnit 5
 * e como usar o Mockito para testes unitários.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Guia Completo de JUnit 5 - Zero Hunger")
class GuiaJUnitTest {

    @Mock
    private List<String> mockList;

    private List<String> realList;

    @BeforeAll
    static void setUpBeforeClass() {
        System.out.println("🚀 Iniciando todos os testes da classe");
    }

    @AfterAll
    static void tearDownAfterClass() {
        System.out.println("✅ Finalizando todos os testes da classe");
    }

    @BeforeEach
    void setUp() {
        realList = new ArrayList<>();
        System.out.println("📝 Preparando teste individual");
    }

    @AfterEach
    void tearDown() {
        System.out.println("🧹 Limpando após teste individual");
    }

    // ========== TESTES BÁSICOS ==========

    @Test
    @DisplayName("Teste básico de igualdade")
    void testeBasicoIgualdade() {
        // Given (Dado)
        String esperado = "Zero Hunger";
        
        // When (Quando)
        String atual = "Zero Hunger";
        
        // Then (Então)
        assertEquals(esperado, atual, "Os valores devem ser iguais");
    }

    @Test
    @DisplayName("Teste de valores nulos")
    void testeValoresNulos() {
        String valorNulo = null;
        String valorNaoNulo = "Não nulo";
        
        assertNull(valorNulo, "Deve ser nulo");
        assertNotNull(valorNaoNulo, "Não deve ser nulo");
    }

    @Test
    @DisplayName("Teste de valores booleanos")
    void testeValoresBooleanos() {
        boolean verdadeiro = true;
        boolean falso = false;
        
        assertTrue(verdadeiro, "Deve ser verdadeiro");
        assertFalse(falso, "Deve ser falso");
    }

    // ========== TESTES COM EXCEÇÕES ==========

    @Test
    @DisplayName("Teste que verifica se exceção é lançada")
    void testeExcecaoLancada() {
        assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Erro intencional");
        }, "Deve lançar IllegalArgumentException");
    }

    @Test
    @DisplayName("Teste que verifica mensagem da exceção")
    void testeMensagemExcecao() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> {
                throw new IllegalArgumentException("Email inválido");
            }
        );
        
        assertEquals("Email inválido", exception.getMessage());
    }

    // ========== TESTES COM COLLECTIONS ==========

    @Test
    @DisplayName("Teste com listas")
    void testeComListas() {
        realList.add("Item 1");
        realList.add("Item 2");
        realList.add("Item 3");
        
        assertEquals(3, realList.size(), "Lista deve ter 3 itens");
        assertTrue(realList.contains("Item 2"), "Lista deve conter 'Item 2'");
        assertFalse(realList.isEmpty(), "Lista não deve estar vazia");
    }

    // ========== TESTES COM MOCKITO ==========

    @Test
    @DisplayName("Teste básico com Mock")
    void testeBasicoComMock() {
        // Given - Configurar comportamento do mock
        when(mockList.size()).thenReturn(5);
        when(mockList.get(0)).thenReturn("Primeiro item");
        
        // When - Executar ação
        int tamanho = mockList.size();
        String primeiroItem = mockList.get(0);
        
        // Then - Verificar resultados
        assertEquals(5, tamanho);
        assertEquals("Primeiro item", primeiroItem);
        
        // Verificar se os métodos foram chamados
        verify(mockList).size();
        verify(mockList).get(0);
    }

    @Test
    @DisplayName("Teste verificando número de chamadas")
    void testeNumeroChamadas() {
        // Given
        when(mockList.add(anyString())).thenReturn(true);
        
        // When
        mockList.add("Item 1");
        mockList.add("Item 2");
        mockList.add("Item 3");
        
        // Then
        verify(mockList, times(3)).add(anyString());
        verify(mockList, never()).clear();
    }

    @Test
    @DisplayName("Teste com ArgumentMatchers")
    void testeComArgumentMatchers() {
        // Given
        when(mockList.contains(anyString())).thenReturn(true);
        when(mockList.contains(eq("item específico"))).thenReturn(false);
        
        // When & Then
        assertTrue(mockList.contains("qualquer string"));
        assertFalse(mockList.contains("item específico"));
        
        verify(mockList).contains("qualquer string");
        verify(mockList).contains("item específico");
    }

    // ========== TESTES PARAMETRIZADOS ==========

    @ParameterizedTest
    @DisplayName("Teste parametrizado com valores")
    @ValueSource(strings = {"admin@zeronhunger.com", "user@zeronhunger.com", "fornecedor@zeronhunger.com"})
    void testeEmailsValidos(String email) {
        assertTrue(email.contains("@"), "Email deve conter @");
        assertTrue(email.contains("zeronhunger.com"), "Email deve ser do domínio zeronhunger.com");
    }

    @ParameterizedTest
    @DisplayName("Teste parametrizado com múltiplos argumentos")
    @CsvSource({
        "1, João, COMPRADOR",
        "2, Maria, FORNECEDOR", 
        "3, Pedro, ADMIN"
    })
    void testeUsuarios(Long id, String nome, String tipo) {
        assertNotNull(id, "ID não deve ser nulo");
        assertNotNull(nome, "Nome não deve ser nulo");
        assertTrue(tipo.matches("COMPRADOR|FORNECEDOR|ADMIN"), "Tipo deve ser válido");
    }

    // ========== TESTES CONDICIONAIS ==========

    @Test
    @DisplayName("Teste executado apenas no Windows")
    @EnabledOnOs(OS.WINDOWS)
    void testeApenasWindows() {
        System.out.println("Este teste roda apenas no Windows");
        assertTrue(true);
    }

    @Test
    @DisplayName("Teste desabilitado temporariamente")
    @Disabled("Funcionalidade ainda em desenvolvimento")
    void testeDesabilitado() {
        // Este teste não será executado
        fail("Este teste não deveria executar");
    }

    // ========== TESTES COM TIMEOUT ==========

    @Test
    @DisplayName("Teste com timeout")
    @Timeout(1) // 1 segundo
    void testeComTimeout() throws InterruptedException {
        // Simula operação rápida
        Thread.sleep(500); // 500ms - deve passar
        assertTrue(true);
    }

    // ========== TESTES ANINHADOS ==========

    @Nested
    @DisplayName("Testes de Validação de Email")
    class TestesValidacaoEmail {
        
        @Test
        @DisplayName("Email válido deve passar")
        void emailValidoDevePassar() {
            String email = "usuario@zeronhunger.com";
            assertTrue(isEmailValido(email));
        }
        
        @Test
        @DisplayName("Email inválido deve falhar")
        void emailInvalidoDeveFalhar() {
            String email = "email-invalido";
            assertFalse(isEmailValido(email));
        }
        
        private boolean isEmailValido(String email) {
            return email != null && email.contains("@") && email.contains(".");
        }
    }

    // ========== DEMONSTRAÇÃO DE ASSERTIONS AVANÇADAS ==========

    @Test
    @DisplayName("Múltiplas assertions em um teste")
    void multiplasAssertions() {
        String nome = "João Silva";
        int idade = 25;
        String email = "joao@zeronhunger.com";
        
        // Executa todas as assertions, mesmo se alguma falhar
        assertAll("Validação de usuário",
            () -> assertNotNull(nome, "Nome não deve ser nulo"),
            () -> assertTrue(idade > 0, "Idade deve ser positiva"),
            () -> assertTrue(email.contains("@"), "Email deve conter @"),
            () -> assertEquals("João Silva", nome, "Nome deve ser exato")
        );
    }

    @Test
    @DisplayName("Teste com mensagem customizada")
    void testeMensagemCustomizada() {
        int valorEsperado = 10;
        int valorAtual = 10; // Corrigido para passar no teste
        
        assertEquals(valorEsperado, valorAtual, 
            () -> "Esperado: " + valorEsperado + ", mas foi: " + valorAtual);
    }
}

/**
 * RESUMO DAS PRINCIPAIS ANNOTATIONS DO JUNIT 5:
 * 
 * @Test - Marca um método como teste
 * @DisplayName - Nome descritivo para o teste
 * @BeforeEach - Executado antes de cada teste
 * @AfterEach - Executado após cada teste
 * @BeforeAll - Executado uma vez antes de todos os testes
 * @AfterAll - Executado uma vez após todos os testes
 * @ParameterizedTest - Teste parametrizado
 * @ValueSource - Fonte de valores para teste parametrizado
 * @CsvSource - Fonte CSV para teste parametrizado
 * @Nested - Agrupa testes relacionados
 * @Disabled - Desabilita um teste
 * @Timeout - Define timeout para o teste
 * @EnabledOnOs - Executa apenas em SO específico
 * 
 * PRINCIPAIS ASSERTIONS:
 * 
 * assertEquals(expected, actual) - Verifica igualdade
 * assertNotEquals(expected, actual) - Verifica diferença
 * assertTrue(condition) - Verifica se é verdadeiro
 * assertFalse(condition) - Verifica se é falso
 * assertNull(object) - Verifica se é nulo
 * assertNotNull(object) - Verifica se não é nulo
 * assertThrows(Exception.class, executable) - Verifica se exceção é lançada
 * assertAll(executables...) - Executa múltiplas assertions
 * 
 * PRINCIPAIS MÉTODOS DO MOCKITO:
 * 
 * when(mock.method()).thenReturn(value) - Define comportamento
 * verify(mock).method() - Verifica se método foi chamado
 * verify(mock, times(n)).method() - Verifica número de chamadas
 * verify(mock, never()).method() - Verifica se nunca foi chamado
 * anyString(), anyInt(), any() - Matchers para argumentos
 * eq(value) - Matcher para valor específico
 */
