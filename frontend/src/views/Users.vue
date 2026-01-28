<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from 'vue-router';
import { fetchUsers, deleteUser, removeToken, isAuthenticated } from "../api/api";

const router = useRouter();
const users = ref([]);
const error = ref("");

const headers = ref([
  { title: 'ID', value: 'userId' },
  { title: 'Name', key: 'name', align: 'start',value: 'userName' },
  { title: 'Age', value: 'userAge' },
  { title: 'Email', value: 'userEmail' },
  { title: 'Phone', value: 'userPhoneNumber' },
  { title: 'Address', value: 'userAddress' },
  { title: 'Actions', value: 'actions', sortable: false }
]);

const handleDelete =async (userId) => {
  try {
    await deleteUser(userId);
    users.value = users.value.filter(user => user.userId !== userId);
  } catch (e) {
    error.value = "Failed to delete user";
  }
};

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
  <div class="header">
      <h2>All Users</h2>
      <button @click="handleLogout" class="logout-btn">Logout</button>
  </div>
    <div class="users-container">
        
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
        
        <v-data-table
          :headers="headers"
          :items="users"
          hover
          fixed-header
          density="comfortable"
          class="elevation-1"
          v-if="users.length > 0"
        >
          <template #item.actions="{ item }">
            <button @click="handleDelete(item.userId)" class="delete-btn">
              Delete
            </button>
          </template>
        </v-data-table>

        <p v-else>No users found</p>
    </div>
</template>

<style>
.users-container {
  padding: 20px;
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: gray;
}

.delete-btn {
  background-color: #ff4d4f;
  color: white;
  padding: 5px 10px;
  cursor: pointer;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #f5f5f5;
}

.logout-btn {
  background-color: #3f3fff;
  color: white;
  padding: 5px 10px;
  cursor: pointer;
}

</style>
