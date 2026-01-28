<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { fetchUsers, updateUser, isAuthenticated, removeToken } from '../api/api'

const router = useRouter()
const route = useRoute()

const userId = route.params.id
const loading = ref(false)
const errorMsg = ref('')
const snackbar = ref(false)

const editedUser = ref({
  userName: '',
  userAge: '',
  userPhoneNumber: '',
  userAddress: '',
  userEmail: '',
})

const handleUpdate = async () => {
  loading.value = true
  errorMsg.value = ''

  try {
    const updatedData = {
      userName: editedUser.value.userName,
      userAge: parseInt(editedUser.value.userAge),
      userPhoneNumber: editedUser.value.userPhoneNumber,
      userAddress: editedUser.value.userAddress,
      userEmail: editedUser.value.userEmail
    }

    await updateUser(userId, updatedData)
    router.push('/')
  } catch (e) {
    errorMsg.value = e.message
    snackbar.value = true
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  if (!isAuthenticated()) {
    router.push('/login')
    return
  }

  try {
    const users = await fetchUsers()
    const user = users.find(u => u.userId === parseInt(userId))

    editedUser.value = {
      userName: user.userName,
      userAge: user.userAge,
      userPhoneNumber: user.userPhoneNumber,
      userAddress: user.userAddress || '',
      userEmail: user.userEmail,
    }
  } catch (e) {
    errorMsg.value = 'Failed to load user data'
    snackbar.value = true
    if (e.message === 'Unauthorized') {
      removeToken()
      router.push('/login')
    }
  }
})

const handleCancel = () => {
  router.push('/')
}
</script>

<template>
    <div class="editUser-container">
        <v-container class="mt-5">
          <v-card max-width="600" class="mx-auto">
            <v-card-title class="text-center">Edit User</v-card-title>
      
            <v-card-text>
              <v-form @submit.prevent="handleUpdate">
                <v-text-field
                  v-model="editedUser.userName"
                  label="Full Name"
                  required
                />
      
                <v-text-field
                  v-model="editedUser.userAge"
                  label="Age"
                  type="number"
                  required
                />
      
                <v-text-field
                  v-model="editedUser.userPhoneNumber"
                  label="Phone Number"
                  required
                />
      
                <v-text-field
                  v-model="editedUser.userAddress"
                  label="Address (Optional)"
                />
      
                <v-text-field
                  v-model="editedUser.userEmail"
                  label="Email"
                  type="email"
                  required
                />
      
                <div >
                  <v-btn type="submit" color="primary" class="mb-4" :loading="loading" block>
                  Update User
                  </v-btn>
                  <v-btn color="grey" @click="handleCancel" block>
                    Cancel
                  </v-btn>
                </div>
              </v-form>
            </v-card-text>
          </v-card>
      
          <v-snackbar v-model="snackbar" :color="errorMsg ? 'error' : 'success'">
            {{ errorMsg }}
          </v-snackbar>
        </v-container>
    </div>
</template>

<style>
.editUser-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: gray;
}
</style>