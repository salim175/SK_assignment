<script setup>
import { onMounted, ref } from "vue";
import { fetchUsers } from "../api/api";

const users = ref([]);
const error = ref("");


onMounted(async () => {
  try {
    users.value = await fetchUsers();
  } catch (e) {
    error.value = "Unable to load users";
  }
});
</script>

<template>
  <div>
    <h2>Users</h2>

    <p v-if="error" style="color:red">{{ error }}</p>

    <ul>
      <li v-for="u in users" :key="u.id">
        {{ u.userId }} - {{ u.userName }} - {{ u.userEmail }} - {{ u.userPhoneNumber }}
      </li>
    </ul>
  </div>
</template>
