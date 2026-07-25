<template>
  <footer class="border-t" style="background: var(--bg-surface); border-color: var(--border-subtle)">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-14">
      <!-- Grid -->
      <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-4 gap-10 mb-12">
        <!-- Brand column -->
        <div class="md:col-span-1">
          <div class="flex items-center gap-2.5 mb-4">
            <div class="w-8 h-8 rounded-xl flex items-center justify-center text-white font-bold text-sm"
                 style="background: var(--gradient-brand)">E</div>
            <span class="text-lg font-bold gradient-text">Edurando</span>
          </div>
          <p class="text-sm leading-relaxed" style="color: var(--text-muted)">
            Connecting students with the world's best educators. Learn anything, anywhere.
          </p>
        </div>

        <!-- Links -->
        <div>
          <h3 class="footer-heading">Links</h3>
          <ul class="space-y-2.5">
            <li v-for="link in links" :key="link.to">
              <router-link :to="link.to" class="footer-link">{{ link.label }}</router-link>
            </li>
          </ul>
        </div>

        <!-- Socials -->
        <div>
          <h3 class="footer-heading">Socials</h3>
          <div class="flex gap-2.5 flex-wrap">
            <a
              v-for="s in socials"
              :key="s.href"
              :href="s.href"
              target="_blank"
              rel="noopener"
              class="social-btn"
            >
              <font-awesome-icon :icon="s.icon" />
            </a>
          </div>
        </div>

        <!-- Contact -->
        <div>
          <h3 class="footer-heading">Contact</h3>
          <ul class="space-y-2.5">
            <li>
              <a href="mailto:kontakt@devvault.de" class="footer-link">
                kontakt@devvault.de
              </a>
            </li>
          </ul>
        </div>
      </div>

      <!-- Bottom bar -->
      <div
        class="flex flex-col sm:flex-row items-center justify-between pt-8 border-t gap-4"
        style="border-color: var(--border-subtle)"
      >
        <p class="text-xs" style="color: var(--text-muted)">© 2026 Edurando. All rights reserved.</p>
        <button
          @click="scrollToTop"
          class="flex items-center gap-2 px-4 py-2 rounded-xl text-xs font-semibold text-white transition-all hover:scale-105 hover:opacity-90"
          style="background: var(--gradient-brand)"
        >
          <font-awesome-icon icon="arrow-up" />
          Back to top
        </button>
      </div>
    </div>
  </footer>
</template>

<script setup>
const links = [
  { to: '/imprint',        label: 'Imprint' },
  { to: '/TermsOfService', label: 'Terms of Service' },
  { to: '/PrivacyPolicy',  label: 'Privacy Policy' },
]

const socials = [
  { href: 'https://www.instagram.com/', icon: 'fab fa-instagram' },
  { href: 'https://www.twitter.com/',   icon: 'fab fa-twitter' },
  { href: 'https://www.facebook.com/',  icon: 'fab fa-facebook' },
  { href: 'https://www.tiktok.com/',    icon: 'fab fa-tiktok' },
]

function scrollToTop() {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}
</script>

<style scoped>
.gradient-text {
  background: var(--gradient-brand);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.footer-heading {
  font-size: 11px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--text-muted);
  margin-bottom: 14px;
}

.footer-link {
  font-size: 13.5px;
  color: var(--text-secondary);
  text-decoration: none;
  transition: color 0.15s ease;
  display: block;
}
.footer-link:hover {
  color: #8b5cf6;
}

.social-btn {
  width: 36px;
  height: 36px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 15px;
  border: 1px solid var(--border-subtle);
  background: var(--bg-elevated);
  color: var(--text-secondary);
  transition: transform 0.15s ease, color 0.15s ease, background 0.15s ease;
  text-decoration: none;
}
.social-btn:hover {
  transform: translateY(-2px) scale(1.08);
  color: #8b5cf6;
  border-color: rgba(139, 92, 246, 0.35);
}
</style>
