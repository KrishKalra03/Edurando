<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useEmailStore } from '@/modules/ResetPassword/emailStore'

const router     = useRouter()
const emailStore = useEmailStore()
const email      = ref('')
const result     = ref('')
const loading    = ref(false)

async function sendEmail() {
  loading.value = true
  result.value  = ''
  try {
    const formData = new FormData()
    formData.append('email', email.value)
    await axios.post('/forgotPassword', formData)
    emailStore.email = email.value
    await router.push('/confirmNumber')
  } catch (error) {
    result.value = error.response?.data || 'Something went wrong. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page">
    <div class="card">

      <div class="icon-wrap">
        <svg viewBox="0 0 48 48" fill="none" aria-hidden="true" width="52" height="52">
          <rect width="48" height="48" rx="14" fill="url(#rpGrad)"/>
          <rect x="10" y="18" width="28" height="20" rx="3" stroke="white" stroke-width="2"/>
          <path d="M10 21l14 10 14-10" stroke="white" stroke-width="2" stroke-linecap="round"/>
          <path d="M24 6v8M20 10l4-4 4 4" stroke="white" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/>
          <defs>
            <linearGradient id="rpGrad" x1="0" y1="0" x2="48" y2="48" gradientUnits="userSpaceOnUse">
              <stop offset="0%"   stop-color="#6366f1"/>
              <stop offset="60%"  stop-color="#8b5cf6"/>
              <stop offset="100%" stop-color="#a855f7"/>
            </linearGradient>
          </defs>
        </svg>
      </div>

      <h1 class="title">Reset Password</h1>
      <p class="subtitle">Enter your email address and we'll send you a verification code.</p>

      <form @submit.prevent="sendEmail" class="form">
        <div class="field">
          <label for="rp-email">Email address</label>
          <input
            id="rp-email"
            v-model="email"
            type="email"
            required
            autocomplete="email"
            placeholder="you@example.com"
          />
        </div>

        <button type="submit" class="btn-primary" :disabled="loading">
          <span v-if="loading" class="spinner"></span>
          <span v-else>Send Code</span>
        </button>

        <p v-if="result" class="error">{{ result }}</p>
      </form>

    </div>
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px 16px;
  background: var(--bg-base);
}

.card {
  background: var(--bg-surface);
  border: 1px solid var(--border-default);
  border-radius: 20px;
  padding: 48px 44px;
  max-width: 420px;
  width: 100%;
  text-align: center;
  box-shadow: var(--shadow-elevated);
}

.icon-wrap { display: flex; justify-content: center; margin-bottom: 24px; }

.title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 10px;
}

.subtitle {
  font-size: 14px;
  line-height: 1.6;
  color: var(--text-secondary);
  margin: 0 0 32px;
}

.form { text-align: left; display: flex; flex-direction: column; gap: 20px; }

.field { display: flex; flex-direction: column; gap: 6px; }

label {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
  letter-spacing: 0.02em;
}

input {
  padding: 12px 14px;
  border-radius: 12px;
  border: 1px solid var(--border-default);
  background: var(--bg-elevated);
  color: var(--text-primary);
  font-size: 14px;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}
input:focus {
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139,92,246,0.12);
}
input::placeholder { color: var(--text-muted); }

.btn-primary {
  width: 100%;
  padding: 13px;
  background: var(--gradient-brand);
  color: #fff;
  font-size: 15px;
  font-weight: 700;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  transition: opacity 0.2s, transform 0.15s;
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 46px;
}
.btn-primary:hover:not(:disabled)  { opacity: 0.88; transform: translateY(-1px); }
.btn-primary:active:not(:disabled) { transform: scale(0.97); }
.btn-primary:disabled { opacity: 0.55; cursor: not-allowed; }

.spinner {
  width: 18px; height: 18px;
  border: 2px solid rgba(255,255,255,0.35);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
  display: inline-block;
}
@keyframes spin { to { transform: rotate(360deg); } }

.error {
  font-size: 13px;
  color: #f87171;
  text-align: center;
  margin: 0;
}
</style>
