<script setup>
import { reactive, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useEmailStore } from '@/modules/ResetPassword/emailStore'

const router     = useRouter()
const emailStore = useEmailStore()
const loading    = ref(false)
const result     = ref('')

const password = reactive({
  email:             emailStore.email,
  newPassword:       '',
  newPasswordRepeat: '',
})

const rules = computed(() => {
  const p = password.newPassword
  return [
    { label: 'At least 8 characters',            ok: p.length >= 8 },
    { label: 'Upper and lowercase letters',       ok: /[a-z]/.test(p) && /[A-Z]/.test(p) },
    { label: 'At least one number',               ok: /\d/.test(p) },
    { label: 'At least one special character',    ok: /[^a-zA-Z0-9]/.test(p) },
  ]
})

const passwordsMatch = computed(() =>
  password.newPassword && password.newPassword === password.newPasswordRepeat
)

const canSubmit = computed(() =>
  rules.value.every(r => r.ok) && passwordsMatch.value
)

async function onSetPassword() {
  if (!canSubmit.value) return
  loading.value = true
  result.value  = ''
  try {
    await axios.post('/resetPassword', password)
    emailStore.email = ''
    await router.push('/login')
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
          <rect width="48" height="48" rx="14" fill="url(#pwGrad)"/>
          <path d="M16 22v-4a8 8 0 0 1 16 0v4" stroke="white" stroke-width="2" stroke-linecap="round"/>
          <rect x="10" y="22" width="28" height="18" rx="3" stroke="white" stroke-width="2"/>
          <circle cx="24" cy="31" r="2.5" fill="white"/>
          <defs>
            <linearGradient id="pwGrad" x1="0" y1="0" x2="48" y2="48" gradientUnits="userSpaceOnUse">
              <stop offset="0%"   stop-color="#6366f1"/>
              <stop offset="60%"  stop-color="#8b5cf6"/>
              <stop offset="100%" stop-color="#a855f7"/>
            </linearGradient>
          </defs>
        </svg>
      </div>

      <h1 class="title">Set New Password</h1>
      <p class="subtitle">Choose a strong password for your account.</p>

      <form @submit.prevent="onSetPassword" class="form">

        <div class="field">
          <label for="new-pw">New Password</label>
          <input
            id="new-pw"
            v-model="password.newPassword"
            type="password"
            autocomplete="new-password"
            placeholder="New password"
            required
          />
        </div>

        <!-- Password rules -->
        <ul v-if="password.newPassword" class="rules">
          <li v-for="r in rules" :key="r.label" :class="r.ok ? 'rule--ok' : 'rule--fail'">
            <span class="rule-icon">{{ r.ok ? '✓' : '✗' }}</span>
            {{ r.label }}
          </li>
        </ul>

        <div class="field">
          <label for="confirm-pw">Confirm Password</label>
          <input
            id="confirm-pw"
            v-model="password.newPasswordRepeat"
            type="password"
            autocomplete="new-password"
            placeholder="Repeat password"
            required
          />
          <p
            v-if="password.newPasswordRepeat && !passwordsMatch"
            class="field-error"
          >Passwords do not match.</p>
        </div>

        <button type="submit" class="btn-primary" :disabled="loading || !canSubmit">
          <span v-if="loading" class="spinner"></span>
          <span v-else>Set Password</span>
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

.form { text-align: left; display: flex; flex-direction: column; gap: 16px; }

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

/* ── Password rules ──────────────────────── */
.rules {
  list-style: none;
  padding: 12px 14px;
  margin: -4px 0 0;
  background: var(--bg-elevated);
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.rules li {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 7px;
}

.rule-icon { font-size: 13px; font-weight: 700; width: 14px; flex-shrink: 0; }
.rule--ok   { color: #4ade80; }
.rule--fail { color: var(--text-muted); }

.field-error {
  font-size: 12px;
  color: #f87171;
  margin: 0;
}

/* ── Button ──────────────────────────────── */
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
  margin-top: 4px;
}
.btn-primary:hover:not(:disabled)  { opacity: 0.88; transform: translateY(-1px); }
.btn-primary:active:not(:disabled) { transform: scale(0.97); }
.btn-primary:disabled { opacity: 0.45; cursor: not-allowed; }

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
