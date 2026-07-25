<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route  = useRoute()
const router = useRouter()

const status = computed(() => route.query.status)
const reason = computed(() => route.query.reason)
const isSuccess = computed(() => status.value === 'success')

const title = computed(() => {
  if (isSuccess.value) return 'Email Verified!'
  if (reason.value === 'expired')           return 'Link Expired'
  if (reason.value === 'already-confirmed') return 'Already Verified'
  return 'Verification Failed'
})

const message = computed(() => {
  if (isSuccess.value)
    return 'Your email has been successfully verified. You can now log in to your Edurando account.'
  if (reason.value === 'expired')
    return 'The verification link has expired. Please request a new one by logging in or trying to register again.'
  if (reason.value === 'already-confirmed')
    return 'Your email address is already verified. You can go ahead and log in.'
  return 'The verification link is invalid or was already used. Please try registering again.'
})

const actionLabel = computed(() =>
  (isSuccess.value || reason.value === 'already-confirmed') ? 'Go to Login' : 'Back to Registration'
)

function handleAction() {
  router.push(
    (isSuccess.value || reason.value === 'already-confirmed') ? '/login' : '/register'
  )
}
</script>

<template>
  <div class="verify-page">
    <div class="verify-card">

      <div class="icon-wrap" :class="isSuccess ? 'icon-wrap--success' : 'icon-wrap--error'">
        <!-- Animated checkmark -->
        <svg v-if="isSuccess" class="anim-icon" viewBox="0 0 52 52" aria-hidden="true">
          <circle class="circle" :class="'circle--success'" cx="26" cy="26" r="24" fill="none" stroke-width="2"/>
          <path  class="check"              fill="none" stroke-width="3"
                 stroke-linecap="round" stroke-linejoin="round"
                 d="M14 27l9 9 15-18"/>
        </svg>
        <!-- Animated X -->
        <svg v-else class="anim-icon" viewBox="0 0 52 52" aria-hidden="true">
          <circle class="circle circle--error" cx="26" cy="26" r="24" fill="none" stroke-width="2"/>
          <path   class="x-line"  fill="none" stroke-width="3"
                  stroke-linecap="round" d="M17 17l18 18"/>
          <path   class="x-line2" fill="none" stroke-width="3"
                  stroke-linecap="round" d="M35 17L17 35"/>
        </svg>
      </div>

      <h1 class="verify-title">{{ title }}</h1>
      <p  class="verify-message">{{ message }}</p>

      <button class="verify-btn" @click="handleAction">
        {{ actionLabel }}
      </button>

    </div>
  </div>
</template>

<style scoped>
.verify-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px 16px;
  background: var(--bg-base);
}

.verify-card {
  background: var(--bg-surface);
  border: 1px solid var(--border-default);
  border-radius: 20px;
  padding: 52px 44px 48px;
  max-width: 440px;
  width: 100%;
  text-align: center;
  box-shadow: var(--shadow-elevated);
}

/* ── Icon ──────────────────────────────────────── */
.icon-wrap {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 32px;
}
.icon-wrap--success { background: rgba(74, 222, 128, 0.12); }
.icon-wrap--error   { background: rgba(248, 113, 113, 0.12); }

.anim-icon { width: 52px; height: 52px; }

/* Circle draw */
.circle {
  stroke-dasharray: 151;
  stroke-dashoffset: 151;
  animation: draw 0.55s cubic-bezier(0.65, 0, 0.45, 1) forwards;
}
.circle--success { stroke: #4ade80; }
.circle--error   { stroke: #f87171; }

/* Checkmark path */
.check {
  stroke: #4ade80;
  stroke-dasharray: 50;
  stroke-dashoffset: 50;
  animation: draw 0.3s cubic-bezier(0.65, 0, 0.45, 1) 0.5s forwards;
}

/* X paths */
.x-line, .x-line2 {
  stroke: #f87171;
  stroke-dasharray: 30;
  stroke-dashoffset: 30;
}
.x-line  { animation: draw 0.3s cubic-bezier(0.65, 0, 0.45, 1) 0.5s  forwards; }
.x-line2 { animation: draw 0.3s cubic-bezier(0.65, 0, 0.45, 1) 0.65s forwards; }

@keyframes draw { to { stroke-dashoffset: 0; } }

/* ── Text ──────────────────────────────────────── */
.verify-title {
  font-size: 26px;
  font-weight: 700;
  color: var(--text-primary);
  margin: 0 0 12px;
  line-height: 1.2;
}

.verify-message {
  font-size: 15px;
  line-height: 1.65;
  color: var(--text-secondary);
  margin: 0 0 36px;
}

/* ── Button ────────────────────────────────────── */
.verify-btn {
  background: var(--gradient-brand);
  color: #fff;
  font-size: 15px;
  font-weight: 700;
  border: none;
  border-radius: 12px;
  padding: 14px 40px;
  cursor: pointer;
  transition: opacity 0.2s, transform 0.15s;
  letter-spacing: 0.01em;
}
.verify-btn:hover  { opacity: 0.88; transform: translateY(-1px); }
.verify-btn:active { transform: scale(0.97); }
</style>
