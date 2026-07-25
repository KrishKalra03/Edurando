<template>
  <div class="auth-page">
    <!-- Background orbs -->
    <div class="orb orb-1"></div>
    <div class="orb orb-2"></div>

    <div class="relative w-full max-w-md mx-auto">
      <div class="auth-card">
        <!-- Icon header -->
        <div class="text-center mb-8">
          <div class="icon-badge mx-auto mb-4">
            <font-awesome-icon icon="fa-solid fa-right-to-bracket" class="text-white text-xl" />
          </div>
          <h1 class="text-2xl font-bold" style="color: var(--text-primary)">Welcome back</h1>
          <p class="text-sm mt-1" style="color: var(--text-muted)">Sign in to your Edurando account</p>
        </div>

        <form @submit.prevent="submit" class="space-y-4">
          <div>
            <label class="field-label">Email</label>
            <input
              type="text"
              v-model="loginRequest.email"
              placeholder="you@example.com"
              required
              class="field-input"
            />
          </div>

          <div>
            <label class="field-label">Password</label>
            <input
              type="password"
              v-model="loginRequest.password"
              placeholder="••••••••"
              required
              class="field-input"
            />
          </div>

          <p v-if="response.length" class="text-red-400 text-sm text-center bg-red-500/10 rounded-xl p-2.5">
            {{ response }}
          </p>

          <div class="text-right">
            <button type="button" @click="resetPassword" class="text-sm font-medium hover:underline" style="color: #8b5cf6; background: none; border: none; cursor: pointer;">
              Forgot password?
            </button>
          </div>

          <button type="submit" class="btn-primary w-full">Sign In</button>

          <div class="text-center pt-1">
            <span class="text-sm" style="color: var(--text-muted)">Don't have an account? </span>
            <button type="button" @click="signUp" class="text-sm font-semibold hover:underline" style="color: #8b5cf6; background: none; border: none; cursor: pointer;">
              Sign Up
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent } from "vue"
import axios from "axios"
import { useUserStore } from "@/store/store"

export default defineComponent({
  name: "LoginPage",
  data() {
    return {
      loginRequest: { email: '', password: '' },
      response: ''
    }
  },
  methods: {
    async submit() {
      try {
        const { data } = await axios.post('/login', this.loginRequest)
        const userStore = useUserStore()
        await userStore.fetchUserById(data.userId)
        userStore.setSession(data.token, userStore.user)
        await this.$router.push({ path: '/' })
        location.reload()
      } catch (error) {
        this.loginRequest.password = ''
        this.response = error.response?.data?.message || error.response?.data || 'Login failed.'
      }
    },
    signUp() {
      this.$router.push({ path: '/register' })
    },
    resetPassword() {
      this.$router.push({ path: '/reset-password' })
    }
  }
})
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 80px 16px 40px;
  background: var(--bg-base);
  position: relative;
  overflow: hidden;
}

.auth-card {
  background: var(--bg-surface);
  border: 1px solid var(--border-default);
  border-radius: 24px;
  padding: 40px;
  box-shadow: var(--shadow-elevated);
  position: relative;
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
  padding: 11px 16px;
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
.field-input::placeholder {
  color: var(--text-muted);
}

.btn-primary {
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
.btn-primary:hover   { opacity: 0.92; transform: scale(1.02); }
.btn-primary:active  { transform: scale(0.98); }

.orb {
  position: fixed;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(90px);
  opacity: 0.12;
}
.orb-1 {
  width: 500px;
  height: 500px;
  top: -100px;
  left: -100px;
  background: radial-gradient(circle, #6366f1, transparent);
}
.orb-2 {
  width: 400px;
  height: 400px;
  bottom: -80px;
  right: -80px;
  background: radial-gradient(circle, #a855f7, transparent);
}
</style>
