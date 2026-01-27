<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from 'vue-router';
import { fetchUsers, removeToken, isAuthenticated } from "../api/api";

const router = useRouter();
const users = ref([]);
const error = ref("");


onMounted(async () => {
  if (!isAuthenticated()) {
    // removeToken();
    router.push("/login");
    return;
  }

  try {
    users.value = await fetchUsers();
  } catch (e) {
    error.value = "Unable to load users";
    if (e.message === 'Unauthorized') {
            removeToken();
            router.push('/login');
        }
  }
});

const handleLogout = () => {
    removeToken();
    router.push('/login');
};
</script>

<template>
    <div class="users-container">
        <div class="header">
            <h2>All Users</h2>
            <button @click="handleLogout" class="logout-btn">Logout</button>
        </div>
        
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
        
        <table v-if="users.length > 0">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Age</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>Address</th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in users" :key="user.userId">
                    <td>{{ user.userId }}</td>
                    <td>{{ user.userName }}</td>
                    <td>{{ user.userAge }}</td>
                    <td>{{ user.userEmail }}</td>
                    <td>{{ user.userPhoneNumber }}</td>
                    <td>{{ user.userAddress }}</td>
                </tr>
            </tbody>
        </table>
        <p v-else>No users found</p>
    </div>
</template>
