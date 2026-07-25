<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useEmailStore } from '@/modules/ResetPassword/emailStore'

const router     = useRouter()
const emailStore = useEmailStore()
const digits     = ref(['', '', '', ''])
const inputRefs  = ref([])
const result     = ref('')
const loading    = ref(false)

function onInput(index) {
  const val = digits.value[index]
  if (!/^\d$/.test(val)) {
    digits.value[index] = ''
    return
  }
  if (index < 3) inputRefs.value[index + 1]?.focus()
}

function onKeydown(event, index) {
  if (event.key === 'Backspace' && !digits.value[index] && index > 0) {
    inputRefs.value[index - 1]?.focus()
  }
}

function onPaste(event) {
  const pasted = event.clipboardData.getData('text').replace(/\D/g, '').slice(0, 4)
  pasted.split('').forEach((ch, i) => { digits.value[i] = ch })
  inputRefs.value[Math.min(pasted.length, 3)]?.focus()
  event.preventDefault()
}

async function confirmNumber() {
  const code = digits.value.join('')
  if (code.length < 4) return
  loading.value = true
  result.value  = ''
  try {
    const formData = new FormData()
    formData.append('email', emailStore.email)
    formData.append('enteredCode', code)
    await axios.post('/confirmCode', formData)
    await router.push('/passwordform')
  } catch (error) {
    result.value = error.response?.data || 'Invalid code. Please try again.'
    digits.value = ['', '', '', '']
    inputRefs.value[0]?.focus()
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
          <rect width="48" height="48" rx="14" fill="url(#otpGrad)"/>
          <path d="M24 10a10 10 0 0 0-10 10v2h-2a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h24a2 2 0 0 0 2-2V24a2 2 0 0 0-2-2h-2v-2a10 10 0 0 0-10-10z" stroke="white" stroke-width="2" fill="none"/>
          <circle cx="24" cy="28" r="2.5" fill="white"/>
          <defs>
            <linearGradient id="otpGrad" x1="0" y1="0" x2="48" y2="48" gradientUnits="userSpaceOnUse">
              <stop offset="0%"   stop-color="#6366f1"/>
              <stop offset="60%"  stop-color="#8b5cf6"/>
              <stop offset="100%" stop-color="#a855f7"/>
            </linearGradient>
          </defs>
        </svg>
      </div>

      <h1 class="title">Enter Code</h1>
      <p class="subtitle">
        We sent a 4-digit code to<br>
        <span class="email-highlight">{{ emailStore.email }}</span>
      </p>

      <form @submit.prevent="confirmNumber">
        <div class="otp-row">
          <input
            v-for="(_, i) in digits"
            :key="i"
            :ref="el => { if (el) inputRefs[i] = el }"
            v-model="digits[i]"
            @input="onInput(i)"
            @keydown="onKeydown($event, i)"
            @paste="onPaste"
            type="text"
            inputmode="numeric"
            maxlength="1"
            class="otp-box"
            autocomplete="off"
          />
        </div>

        <button type="submit" class="btn-primary" :disabled="loading || digits.join('').length < 4">
          <span v-if="loading" class="spinner"></span>
          <span v-else>Verify Code</span>
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
  line-height: 1.7;
  color: var(--text-secondary);
  margin: 0 0 32px;
}

.email-highlight {
  color: var(--text-primary);
  font-weight: 600;
  word-break: break-all;
}

/* ── OTP ─────────────────────────────────── */
.otp-row {
  display: flex;
  justify-content: center;
  gap: 12px;
  margin-bottom: 32px;
}

.otp-box {
  width: 64px;
  height: 72px;
  border-radius: 14px;
  border: 2px solid var(--border-default);
  background: var(--bg-elevated);
  color: var(--text-primary);
  font-size: 28px;
  font-weight: 700;
  text-align: center;
  outline: none;
  transition: border-color 0.2s, box-shadow 0.2s;
  caret-color: transparent;
}
.otp-box:focus {
  border-color: #8b5cf6;
  box-shadow: 0 0 0 3px rgba(139,92,246,0.15);
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
  margin-top: 14px;
}
</style>
