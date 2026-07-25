<template>
  <div class="auth-page">
    <div class="orb orb-1"></div>
    <div class="orb orb-2"></div>

    <div class="relative w-full max-w-md mx-auto">
      <div class="auth-card">
        <!-- Header -->
        <div class="text-center mb-8">
          <div class="icon-badge mx-auto mb-4">
            <font-awesome-icon icon="fa-solid fa-user" class="text-white text-xl" />
          </div>
          <h1 class="text-2xl font-bold" style="color: var(--text-primary)">Create an account</h1>
          <p class="text-sm mt-1" style="color: var(--text-muted)">Join Edurando and start learning</p>
        </div>

        <form @submit.prevent="registerUser" class="space-y-4">
          <!-- Role -->
          <div>
            <label class="field-label">Role</label>
            <select v-model="user.role" required class="field-input">
              <option value="" disabled>Select role</option>
              <option value="Student">Student</option>
              <option value="Teacher">Teacher</option>
            </select>
          </div>

          <!-- Name row -->
          <div class="grid grid-cols-2 gap-3">
            <div>
              <label class="field-label">First Name</label>
              <input type="text" v-model="user.firstName" placeholder="Jane" required class="field-input" />
            </div>
            <div>
              <label class="field-label">Last Name</label>
              <input type="text" v-model="user.lastName" placeholder="Doe" required class="field-input" />
            </div>
          </div>

          <!-- Email -->
          <div>
            <label class="field-label">Email address</label>
            <input type="email" v-model="user.email" placeholder="you@example.com" required autocomplete="off" class="field-input" />
          </div>

          <!-- Password -->
          <div>
            <label class="field-label">Password</label>
            <input
              type="password"
              v-model="user.password"
              placeholder="••••••••"
              required
              autocomplete="off"
              class="field-input"
              :class="passwordBorderClass(user.password, user.passwordRepeat, 'password')"
            />
          </div>

          <!-- Repeat password -->
          <div>
            <label class="field-label">Repeat Password</label>
            <input
              type="password"
              v-model="user.passwordRepeat"
              placeholder="••••••••"
              required
              autocomplete="off"
              class="field-input"
              :class="passwordBorderClass(user.password, user.passwordRepeat, 'repeat')"
            />
          </div>

          <!-- Password error hints -->
          <div v-if="passwordErrors.length" class="rounded-xl p-3 space-y-1" style="background: rgba(239,68,68,0.06); border: 1px solid rgba(239,68,68,0.15)">
            <p v-for="e in passwordErrors" :key="e" class="flex items-center gap-1.5 text-xs text-red-400">
              <font-awesome-icon :icon="['fa', 'xmark']" />
              {{ e }}
            </p>
          </div>

          <!-- Checkboxes -->
          <div class="space-y-2 pt-1">
            <label class="flex items-start gap-2.5 cursor-pointer">
              <input type="checkbox" v-model="user.termsAgreed" required class="mt-0.5 accent-violet-500" />
              <span class="text-sm" style="color: var(--text-secondary)">
                I agree to the
                <RouterLink to="/TermsOfService" target="_blank" class="text-violet-500 hover:underline">Terms of Service</RouterLink>
              </span>
            </label>
            <label class="flex items-start gap-2.5 cursor-pointer">
              <input type="checkbox" v-model="user.privacyAgreed" required class="mt-0.5 accent-violet-500" />
              <span class="text-sm" style="color: var(--text-secondary)">
                I agree to the
                <RouterLink to="/PrivacyPolicy" target="_blank" class="text-violet-500 hover:underline">Privacy Policy</RouterLink>
              </span>
            </label>
          </div>

          <button type="submit" class="btn-primary w-full mt-2">Create Account</button>

          <div class="text-center pt-1">
            <span class="text-sm" style="color: var(--text-muted)">Already have an account? </span>
            <RouterLink to="/login" class="text-sm font-semibold hover:underline" style="color: #8b5cf6">Sign In</RouterLink>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, computed } from "vue"
import axios from 'axios'
import { useRouter } from "vue-router"
import { showPasswordError, isPasswordValid, isPasswordRepeatValid } from '@/functions/functions'
import { useEmailStore } from "@/modules/Registration/emailStore"

const result = ref("")
const router = useRouter()
const user = reactive({
  role: '',
  firstName: '',
  lastName: '',
  email: '',
  password: '',
  passwordRepeat: '',
  termsAgreed: false,
  privacyAgreed: false
})
const emailStore = useEmailStore()

const passwordErrors = computed(() => showPasswordError(user.password, user.passwordRepeat))

function passwordBorderClass(pw, repeat, field) {
  if (field === 'password') {
    if (!pw.length) return ''
    return isPasswordValid(pw, repeat) ? 'border-green-500' : 'border-red-400'
  }
  if (!repeat.length) return ''
  return isPasswordRepeatValid(pw, repeat) ? 'border-green-500' : 'border-red-400'
}

async function registerUser() {
  try {
    const response = await axios.post('/register', user)
    result.value = response.data
    if (result.value) {
      emailStore.email = user.email
    }
    await router.push('/confirm')
  } catch (error) {
    result.value = error.request.response
    console.log(result.value)
  }
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 90px 16px 40px;
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
  appearance: none;
  transition: border-color 0.2s, box-shadow 0.2s;
}
.field-input:focus {
  border-color: #8b5cf6 !important;
  box-shadow: 0 0 0 3px rgba(139, 92, 246, 0.12);
}
.field-input::placeholder {
  color: var(--text-muted);
}

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

.orb {
  position: fixed;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(90px);
  opacity: 0.10;
}
.orb-1 {
  width: 500px; height: 500px;
  top: -100px; left: -100px;
  background: radial-gradient(circle, #6366f1, transparent);
}
.orb-2 {
  width: 400px; height: 400px;
  bottom: -80px; right: -80px;
  background: radial-gradient(circle, #a855f7, transparent);
}
</style>
