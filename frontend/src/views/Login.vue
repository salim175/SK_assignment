<script setup>
import { loginUser } from '@/api/api'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const email = ref('')
const password = ref('')
const loading = ref(false)
const errorMsg = ref('')
const snackbar = ref(false)
const visible = ref(false)

const handleLogin = async () => {
  loading.value = true
  errorMsg.value = ''

  try {
    const res = await loginUser({
      email: email.value,
      password: password.value,
    })

    console.log(res)

    if (!res.ok) {
      errorMsg.value = res.data.message
      snackbar.value = true
      loading.value = false
      return
    }

    const user = res.data

    localStorage.setItem("user", JSON.stringify(user));

    router.push('/users')
  } catch (e) {
    errorMsg.value = 'Network error'
    snackbar.value = true
  } finally {
    loading.value = false
  }
}
</script>

<template>
    <div class="login-container">
        <v-container class="d-flex align-center justify-center min-h-screen">
          <v-card width="400" class="pa-6">
            <v-card-title class="text-center text-h5 mb-3">Salim's Assignment</v-card-title>
      
            <v-form @submit.prevent="handleLogin">
              <v-text-field
                  v-model="email"
                  density="comfortable"
                  placeholder="Email address"
                  prepend-inner-icon="mdi-email-outline"
                  variant="outlined"
                  required
              />
      
              <v-text-field
                v-model="password"
                :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
                :type="visible ? 'text' : 'password'"
                density="comfortable"
                placeholder="Enter your password"
                prepend-inner-icon="mdi-lock-outline"
                variant="outlined"
                @click:append-inner="visible = !visible"
                required
              />
      
              <v-btn
                  type="submit"
                  class="mb-8"
                  color="#3f3fff"
                  size="large"
                  block
                  :loading="loading"
              >
                Login
              </v-btn>
      
              <p class="text-center">Don't have an account?</p>
              <v-card-text class="text-center pa-0">
                  <router-link
                  class="text-blue text-decoration-none"
                  to="/register"
                  rel="noopener noreferrer"
                  >
                  Sign up now <v-icon icon="mdi-chevron-right"></v-icon>
                  </router-link>
              </v-card-text>
      
              <v-snackbar v-model="snackbar" color="red">
                {{ errorMsg }}
              </v-snackbar>
            </v-form>
          </v-card>
        </v-container>
    </div>
</template>

<style>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: gray;
}
</style>

