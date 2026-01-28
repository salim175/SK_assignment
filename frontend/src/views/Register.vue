<script setup>
import { registerUser } from '@/api/api'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const userName = ref('')
const userAge = ref('')
const userPhoneNumber = ref('')
const userAddress = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const loading = ref(false)
const errorMsg = ref('')
const snackbar = ref(false)
const visible = ref(false)

const handleRegister = async () => {
  if (password.value !== confirmPassword.value) {
    errorMsg.value = 'Passwords do not match'
    snackbar.value = true
    return
  }

  if (password.value.length < 6) {
    errorMsg.value = 'Password must be at least 6 characters'
    snackbar.value = true
    return
  }

  loading.value = true
  errorMsg.value = ''

  try {
    const res = await registerUser({
      userName: userName.value,
      userAge: parseInt(userAge.value),
      userPhoneNumber: userPhoneNumber.value,
      userAddress: userAddress.value,
      userEmail: email.value,
      userPassword: password.value,
    })

    if (!res.ok) {
      errorMsg.value = res.data.message
      snackbar.value = true
      loading.value = false
      return
    }

    const user = res.data
    console.log('createduser', user)

    // Automatically log in the user
    localStorage.setItem('user', JSON.stringify(user))
    
    router.push('/')
  } catch (e) {
    errorMsg.value = 'Network error'
    snackbar.value = true
  } finally {
    loading.value = false
  }
}
</script>

<template>
    <div class="register-container">
        <v-container class="d-flex align-center justify-center min-h-screen">
          <v-card width="500" class="pt-2 pa-8">
            <div class="text-center mb-2">
                <v-card-title class="text-h5 ">Create Account</v-card-title>
            </div>
      
            <v-form @submit.prevent="handleRegister">
              <v-text-field
                v-model="userName"
                label="Full Name"
                variant="outlined"
                density="comfortable"
                required
                :rules="[(v) => !!v || 'Name is required']"
              />
      
              <v-text-field
                v-model="userAge"
                label="Age"
                variant="outlined"
                density="comfortable"
                type="number"
                required
                :rules="[(v) => !!v || 'Age is required', (v) => v > 0 || 'Age must be positive']"
              />
      
              <v-text-field
                v-model="userPhoneNumber"
                label="Phone Number"
                variant="outlined"
                density="comfortable"
                required
                :rules="[(v) => !!v || 'Phone number is required']"
              />
      
              <v-text-field
                v-model="userAddress"
                label="Address (Optional)"
                variant="outlined"
                density="comfortable"
              />
      
              <v-text-field
                v-model="email"
                label="Email"
                variant="outlined"
                density="comfortable"
                type="email"
                required
                :rules="[(v) => !!v || 'Email is required', (v) => /.+@.+\..+/.test(v) || 'Email must be valid']"
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
                :rules="[(v) => !!v || 'Password is required']"
              />
      
              <v-text-field
                v-model="confirmPassword"
                :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
                :type="visible ? 'text' : 'password'"
                density="comfortable"
                placeholder="Confirm your password"
                prepend-inner-icon="mdi-lock-outline"
                variant="outlined"
                @click:append-inner="visible = !visible"
                required
                :rules="[(v) => !!v || 'Please confirm password']"
              />
      
              <v-btn
                type="submit"
                class="mb-8"
                color="#3f3fff"
                size="large"
                block
                :loading="loading"
              >
                Register
              </v-btn>
      
              <div class="text-center mt-4">
                <span>Already have an account? </span>
                <a @click="router.push('/login')" class="text-primary cursor-pointer">Login here</a>
              </div>
      
              <v-snackbar v-model="snackbar" :color="errorMsg ? 'red' : 'green'">
                {{ errorMsg }}
              </v-snackbar>
            </v-form>
          </v-card>
        </v-container>
    </div>
</template>

<style scoped>
.cursor-pointer {
  cursor: pointer;
}

.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: gray;
}
</style>