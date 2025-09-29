<template>
  <div class="user-registration">
    <el-card class="box-card">
      <div class="registration-header">
        <h2><i class="fa fa-user-plus"></i> Cadastro de Usuário</h2>
      </div>

      <el-form :model="form" ref="form" :rules="rules" label-width="120px">
        <el-form-item label="Nome" prop="nome">
          <el-input v-model="form.nome" placeholder="Digite seu nome"></el-input>
        </el-form-item>

        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" placeholder="Digite seu email"></el-input>
        </el-form-item>

        <el-form-item label="Usuário" prop="usuario">
          <el-input v-model="form.usuario" placeholder="Digite seu usuário"></el-input>
        </el-form-item>

        <el-form-item label="Senha" prop="senha">
          <el-input v-model="form.senha" type="password" placeholder="Digite sua senha"></el-input>
        </el-form-item>

        <el-form-item label="Confirmar Senha" prop="confirmarSenha">
          <el-input v-model="form.confirmarSenha" type="password" placeholder="Confirme sua senha"></el-input>
        </el-form-item>

        <el-form-item label="Data de Nascimento" prop="dataNascimento">
          <el-date-picker v-model="form.dataNascimento" type="date" placeholder="Selecione a data de nascimento"></el-date-picker>
        </el-form-item>

        <el-form-item label="Telefone" prop="telefone">
          <el-input v-model="form.telefone" placeholder="Digite seu telefone"></el-input>
        </el-form-item>

        <el-form-item label="Logradouro" prop="logradouro">
          <el-input v-model="form.logradouro" placeholder="Digite o logradouro"></el-input>
        </el-form-item>

        <el-form-item label="Número" prop="numero">
          <el-input v-model="form.numero" placeholder="Digite o número"></el-input>
        </el-form-item>

        <el-form-item label="Tipo de Usuário" prop="tipoUsuario">
          <el-select v-model="form.tipoUsuario" placeholder="Selecione o tipo">
            <el-option label="Cliente" value="cliente"></el-option>
            <el-option label="Fornecedor" value="fornecedor"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm">Criar</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'UserRegistration',
  data() {
    return {
      apiUrl: process.env.VUE_APP_API_MODE,
      form: {
        nome: '',
        email: '',
        usuario: '',
        senha: '',
        confirmarSenha: '',
        tipoUsuario: '',
        dataNascimento: null,
        telefone: '',
        logradouro: '', // Novo campo para logradouro
        numero: '' // Novo campo para número
      },
      rules: {
        nome: [{ required: true, message: 'Por favor, insira seu nome', trigger: 'blur' }],
        email: [{ required: true, type: 'email', message: 'Por favor, insira um email válido', trigger: 'blur' }],
        usuario: [{ required: true, message: 'Por favor, insira um nome de usuário', trigger: 'blur' }],
        senha: [{ required: true, message: 'Por favor, insira uma senha', trigger: 'blur' }],
        confirmarSenha: [
          { required: true, message: 'Por favor, confirme sua senha', trigger: 'blur' },
          { validator: this.validatePassword, trigger: 'blur' }
        ],
        dataNascimento: [{ required: true, message: 'Por favor, insira sua data de nascimento', trigger: 'change' }],
        telefone: [{ required: true, message: 'Por favor, insira seu telefone', trigger: 'blur' }],
        logradouro: [{ required: true, message: 'Por favor, insira o logradouro', trigger: 'blur' }],
        numero: [{ required: true, message: 'Por favor, insira o número', trigger: 'blur' }],
        tipoUsuario: [{ required: true, message: 'Por favor, selecione um tipo de usuário', trigger: 'change' }]
      }
    };
  },
  methods: {
    submitForm() {
      this.$refs.form.validate((valid) => {
        if (valid) {
          // Concatena logradouro e número
          const endereco = `${this.form.logradouro} N ${this.form.numero}`;

          const userData = {
            nome: this.form.nome,
            email: this.form.email,
            usuario: this.form.usuario,
            senha: this.form.senha,
            tipo: this.form.tipoUsuario,
            dataNascimento: this.form.dataNascimento,
            telefone: this.form.telefone,
            endereco // Envia o endereço concatenado
          };

          // Enviar os dados para o backend via JSON
          this.sendDataToBackend(userData);
        } else {
          this.$message.error('Por favor, preencha corretamente os campos.');
          return false;
        }
      });
    },
    validatePassword(rule, value, callback) {
      if (value !== this.form.senha) {
        callback(new Error('As senhas não coincidem'));
      } else {
        callback();
      }
    },
    sendDataToBackend(userData) {
      fetch(`${this.apiUrl}/usuarios/criar`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(userData)
      })
        .then(response => {
          if (!response.ok) {
            return response.json().then(err => { throw new Error(err.message); });
          }
          return response.json();
        })
        .then(data => {
          this.$message.success('Usuário cadastrado com sucesso!');
          console.log('Usuário cadastrado:', data);
        })
        .catch(error => {
          this.$message.error('Erro ao cadastrar usuário: ' + error.message);
          console.error('Erro:', error);
        });
    }
  }
};
</script>

<style scoped>
.user-registration {
  max-width: 600px;
  margin: 50px auto;
}

.registration-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.fa-user-plus {
  margin-right: 10px;
}

.el-input, .el-date-picker {
  width: 100%;
}

.el-button {
  width: 100%;
  margin-top: 10px;
}
</style>
