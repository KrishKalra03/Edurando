<template>
  <edit-page></edit-page>

  <div class="auth-page">
    <div class="relative w-full max-w-md mx-auto">
      <div class="auth-card">
        <div class="text-center mb-8">
          <div class="icon-badge mx-auto mb-4">
            <font-awesome-icon icon="fa-solid fa-key" class="text-white text-xl" />
          </div>
          <h1 class="text-2xl font-bold" style="color: var(--text-primary)">Change Password</h1>
          <p class="text-sm mt-1" style="color: var(--text-muted)">Keep your account secure</p>
        </div>

        <form @submit.prevent="editPassword" class="space-y-4">
          <div>
            <label class="field-label">Current Password</label>
            <input type="password" v-model="password.currentPassword" placeholder="••••••••" required class="field-input" />
          </div>
          <div>
            <label class="field-label">New Password</label>
            <input type="password" v-model="password.newPassword" placeholder="••••••••" required class="field-input" />
            <p v-if="password.newPassword.length > 0 && showPasswordError(password.newPassword, password.newPasswordRepeat).length"
               class="text-red-400 text-xs mt-1.5">
              Choose a stronger password (min. 8 chars, mixed case, number).
            </p>
          </div>
          <div>
            <label class="field-label">Repeat New Password</label>
            <input type="password" v-model="password.newPasswordRepeat" placeholder="••••••••" required class="field-input" />
          </div>

          <p v-if="result.length" class="text-red-400 text-sm text-center bg-red-500/10 rounded-xl p-2.5">{{ result }}</p>

          <button type="submit" class="btn-primary w-full mt-2">Save Changes</button>
        </form>
      </div>
    </div>
  </div>

  <Footer />
</template>

<script setup>
import { reactive, ref } from 'vue'
import axios from 'axios'
import EditPage from '@/modules/UserUpdate/EditPage.vue'
import Footer from "@/modules/Footer/Footer.vue"
import { useRouter } from "vue-router"
import { showPasswordError } from '@/functions/functions'
import { useUserStore } from "@/store/store"

const result = ref('')
const router = useRouter()
const userStore = useUserStore()
const password = reactive({
  id: userStore.getUser.id,
  currentPassword: '',
  newPassword: '',
  newPasswordRepeat: '',
})

async function editPassword() {
  try {
    const response = await axios.put('/editPassword', password)
    result.value = response.data
    await router.push('/')
  } catch (error) {
    result.value = error.response.data
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 16px;
  background: var(--bg-base);
}

.auth-card {
  background: var(--bg-surface);
  border: 1px solid var(--border-default);
  border-radius: 24px;
  padding: 40px;
  box-shadow: var(--shadow-elevated);
}

.icon-badge {
  width: 56px;
  height: 56px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--gradient-brand);
}

.field-label {
  display: block;
  font-size: 13px;
  font-weight: 500;
  margin-bottom: 6px;
  color: var(--text-secondary);
}

.field-input {
  width: 100%;
  padding: 11px 14px;
  border-radius: 12px;
  border: 1px solid var(--border-subtle);
  background: var(--bg-elevated);
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}
.field-input:focus {
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.12);
}
.field-input::placeholder { color: var(--text-muted); }

.btn-primary {
  display: block;
  padding: 12px 24px;
  border-radius: 12px;
  font-size: 14px;
  font-weight: 600;
  color: white;
  border: none;
  cursor: pointer;
  background: var(--gradient-brand);
  transition: opacity 0.2s, transform 0.15s;
}
.btn-primary:hover  { opacity: 0.92; transform: scale(1.02); }
.btn-primary:active { transform: scale(0.98); }
</style>
