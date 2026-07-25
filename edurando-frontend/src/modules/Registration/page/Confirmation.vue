<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useEmailStore } from '@/modules/Registration/emailStore'

const emailStore = useEmailStore()
const email      = ref(emailStore.email)
const result     = ref('')
const loading    = ref(false)
const isError    = ref(false)

async function resendEmail() {
  loading.value = true
  result.value  = ''
  isError.value = false
  try {
    const res = await axios.post(`/reconfirm?email=${email.value}`)
    result.value = res.data
  } catch (e) {
    result.value = e.response?.data || 'Something went wrong. Please try again.'
    isError.value = true
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="confirm-page">
    <div class="confirm-card">

      <!-- Icon -->
      <div class="icon-wrap">
        <svg viewBox="0 0 48 48" fill="none" aria-hidden="true" class="mail-icon">
          <rect width="48" height="48" rx="14" fill="url(#mailGrad)"/>
          <rect x="10" y="14" width="28" height="20" rx="3" stroke="white" stroke-width="2"/>
          <path d="M10 17l14 10 14-10" stroke="white" stroke-width="2" stroke-linecap="round"/>
          <defs>
            <linearGradient id="mailGrad" x1="0" y1="0" x2="48" y2="48" gradientUnits="userSpaceOnUse">
              <stop offset="0%"   stop-color="#6366f1"/>
              <stop offset="60%"  stop-color="#8b5cf6"/>
              <stop offset="100%" stop-color="#a855f7"/>
            </linearGradient>
          </defs>
        </svg>
      </div>

      <h1 class="confirm-title">Check your inbox</h1>

      <p class="confirm-body">
        We've sent a verification link to
        <span class="confirm-email">{{ email }}</span>.
        Click the link in the email to activate your account.
      </p>

      <p class="confirm-hint">
        Can't find the email? Check your spam folder.
      </p>

      <button class="resend-btn" :disabled="loading" @click="resendEmail">
        <span v-if="loading" class="spinner"></span>
        <span v-else>Resend email</span>
      </button>

      <p v-if="result" class="feedback" :class="isError ? 'feedback--error' : 'feedback--ok'">
        {{ result }}
      </p>

    </div>
  </div>
</template>

<style scoped>
.confirm-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px 16px;
  background: var(--bg-base);
}

.confirm-card {
  background: var(--bg-surface);
  border: 1px solid var(--border-default);
  border-radius: 20px;
  padding: 48px 44px;
  max-width: 440px;
  width: 100%;
  text-align: center;
  box-shadow: var(--shadow-elevated);
}

/* ── Icon ──────────────────────────────────────── */
.icon-wrap {
  display: flex;
  justify-content: center;
  margin-bottom: 28px;
}

.mail-icon {
  width: 56px;
  height: 56px;
}

/* ── Text ──────────────────────────────────────── */
.confirm-title {
  font-size: 24px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 14px;
  line-height: 1.2;
}

.confirm-body {
  font-size: 15px;
  line-height: 1.65;
  color: var(--text-secondary);
  margin: 0 0 10px;
}

.confirm-email {
  color: var(--text-primary);
  font-weight: 600;
  word-break: break-all;
}

.confirm-hint {
  font-size: 13px;
  color: var(--text-muted);
  margin: 0 0 32px;
}

/* ── Button ────────────────────────────────────── */
.resend-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: var(--gradient-brand);
  color: #fff;
  font-size: 15px;
  font-weight: 700;
  border: none;
  border-radius: 12px;
  padding: 13px 36px;
  cursor: pointer;
  transition: opacity 0.2s, transform 0.15s;
  letter-spacing: 0.01em;
  min-width: 160px;
  min-height: 46px;
}
.resend-btn:hover:not(:disabled)  { opacity: 0.88; transform: translateY(-1px); }
.resend-btn:active:not(:disabled) { transform: scale(0.97); }
.resend-btn:disabled { opacity: 0.55; cursor: not-allowed; }

/* ── Spinner ───────────────────────────────────── */
.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255,255,255,0.35);
  border-top-color: #fff;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
  display: inline-block;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* ── Feedback ──────────────────────────────────── */
.feedback {
  margin-top: 16px;
  font-size: 13px;
  line-height: 1.5;
}
.feedback--ok    { color: #4ade80; }
.feedback--error { color: #f87171; }
</style>
