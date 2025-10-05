# 🔐 Configuração de Secrets para GitHub Actions

Este documento explica como configurar os secrets necessários para que os pipelines de CI/CD funcionem corretamente.

## 📋 Secrets Obrigatórios

### 🐳 Docker Hub
```
DOCKER_USERNAME: seu-usuario-dockerhub
DOCKER_PASSWORD: sua-senha-dockerhub
```

**Como configurar:**
1. Acesse [Docker Hub](https://hub.docker.com/)
2. Crie uma conta (se não tiver)
3. Vá em Settings → Security → Access Tokens
4. Crie um novo token com permissões de Read/Write

### 📊 SonarCloud (Opcional)
```
SONAR_TOKEN: seu-token-sonarcloud
```

**Como configurar:**
1. Acesse [SonarCloud](https://sonarcloud.io/)
2. Faça login com GitHub
3. Crie um novo projeto
4. Vá em Administration → Security
5. Gere um token

### 📈 Codecov (Opcional)
```
CODECOV_TOKEN: seu-token-codecov
```

**Como configurar:**
1. Acesse [Codecov](https://codecov.io/)
2. Faça login com GitHub
3. Adicione seu repositório
4. Copie o token do projeto

### 🛡️ Snyk (Opcional)
```
SNYK_TOKEN: seu-token-snyk
```

**Como configurar:**
1. Acesse [Snyk](https://snyk.io/)
2. Crie uma conta
3. Vá em Settings → API Token
4. Gere um novo token

### 🌐 Netlify (Opcional - Deploy Preview)
```
NETLIFY_AUTH_TOKEN: seu-token-netlify
NETLIFY_SITE_ID: id-do-seu-site
```

**Como configurar:**
1. Acesse [Netlify](https://netlify.com/)
2. Crie uma conta
3. Vá em User Settings → Applications → Personal Access Tokens
4. Gere um token
5. Crie um site e copie o Site ID

## ⚙️ Como Adicionar Secrets no GitHub

1. **Acesse seu repositório no GitHub**
2. **Vá em Settings → Secrets and variables → Actions**
3. **Clique em "New repository secret"**
4. **Adicione cada secret com o nome exato listado acima**

## 🚀 Testando os Pipelines

### Sem Secrets (Básico)
Os pipelines funcionarão parcialmente sem secrets:
- ✅ Build e testes
- ✅ Análise de código básica
- ❌ Docker push
- ❌ Deploy automático

### Com Secrets (Completo)
Com todos os secrets configurados:
- ✅ Build e testes
- ✅ Análise de código completa
- ✅ Docker push
- ✅ Deploy automático
- ✅ Métricas e relatórios

## 🔧 Configuração Mínima Recomendada

Para um portfólio profissional, configure pelo menos:

```
DOCKER_USERNAME
DOCKER_PASSWORD
SONAR_TOKEN (opcional mas recomendado)
```

## 📝 Exemplo de Configuração

```yaml
# No GitHub: Settings → Secrets → Actions
DOCKER_USERNAME: "fellps"
DOCKER_PASSWORD: "dckr_pat_xxxxxxxxxxxxx"
SONAR_TOKEN: "sqp_xxxxxxxxxxxxx"
```

## 🆘 Troubleshooting

### Pipeline falha no Docker push
- ✅ Verifique DOCKER_USERNAME e DOCKER_PASSWORD
- ✅ Confirme que o token tem permissões de escrita

### SonarCloud não funciona
- ✅ Verifique se o projeto foi criado no SonarCloud
- ✅ Confirme o SONAR_TOKEN
- ✅ Atualize organization no build.gradle

### Netlify deploy falha
- ✅ Verifique NETLIFY_AUTH_TOKEN e NETLIFY_SITE_ID
- ✅ Confirme que o site foi criado no Netlify

---

💡 **Dica**: Comece sem secrets para testar os builds básicos, depois adicione gradualmente para funcionalidades avançadas!
