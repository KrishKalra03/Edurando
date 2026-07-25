<template>
  <div class="min-h-screen pt-24 pb-16 px-4 sm:px-6 lg:px-8" style="background: var(--bg-base)">
    <div class="max-w-3xl mx-auto">

      <!-- Header -->
      <div class="mb-12">
        <p class="text-xs font-semibold uppercase tracking-widest mb-2" style="color: var(--text-muted)">Legal</p>
        <h1 class="text-4xl font-extrabold mb-3" style="color: var(--text-primary)">Privacy Policy</h1>
        <p class="text-sm" style="color: var(--text-muted)">Last updated: 1 July 2024</p>
        <div class="mt-6 h-px" style="background: var(--border-subtle)"></div>
      </div>

      <div class="space-y-10">

        <section>
          <p style="color: var(--text-secondary)">
            Edurando ("Edurando", "we", "us", or "our") is committed to protecting your personal data.
            This Privacy Policy explains what data we collect, why we collect it, how we use it, and what
            rights you have under the General Data Protection Regulation (GDPR) and applicable German law.
          </p>
        </section>

        <section>
          <h2 class="section-title">1. Data Controller</h2>
          <div class="p-4 rounded-xl" style="background: var(--bg-surface); border: 1px solid var(--border-subtle)">
            <p style="color: var(--text-primary)" class="font-medium">Edurando</p>
            <a href="mailto:info.edurando@gmail.com" class="text-link">info.edurando@gmail.com</a>
          </div>
        </section>

        <section>
          <h2 class="section-title">2. Data We Collect</h2>
          <p class="mb-3" style="color: var(--text-secondary)">
            We collect personal data that you provide to us directly or that is generated through your use of the platform:
          </p>
          <div class="space-y-3">
            <div class="p-4 rounded-xl" style="background: var(--bg-surface); border: 1px solid var(--border-subtle)">
              <p class="font-semibold text-sm mb-1" style="color: var(--text-primary)">Account Data</p>
              <p class="text-sm" style="color: var(--text-secondary)">
                First and last name, email address, password (stored as a bcrypt hash), role (student or teacher), and gender.
              </p>
            </div>
            <div class="p-4 rounded-xl" style="background: var(--bg-surface); border: 1px solid var(--border-subtle)">
              <p class="font-semibold text-sm mb-1" style="color: var(--text-primary)">Profile Data</p>
              <p class="text-sm" style="color: var(--text-secondary)">
                Biography, profile picture, subjects, topics, mobile number, and address (street, house number, city, state, postal code).
              </p>
            </div>
            <div class="p-4 rounded-xl" style="background: var(--bg-surface); border: 1px solid var(--border-subtle)">
              <p class="font-semibold text-sm mb-1" style="color: var(--text-primary)">Usage Data</p>
              <p class="text-sm" style="color: var(--text-secondary)">
                Chat messages sent via our platform, ratings and reviews you submit, and search queries.
              </p>
            </div>
          </div>
        </section>

        <section>
          <h2 class="section-title">3. Legal Basis for Processing</h2>
          <p class="mb-3" style="color: var(--text-secondary)">
            We process your personal data on the following legal bases under Art. 6 GDPR:
          </p>
          <ul class="list-disc pl-5 space-y-1.5" style="color: var(--text-secondary)">
            <li><strong style="color: var(--text-primary)">Contract (Art. 6(1)(b)):</strong> Processing necessary to provide and maintain your account and the services you request.</li>
            <li><strong style="color: var(--text-primary)">Consent (Art. 6(1)(a)):</strong> Where you have given explicit consent, e.g. when agreeing to these terms during registration.</li>
            <li><strong style="color: var(--text-primary)">Legitimate interests (Art. 6(1)(f)):</strong> To improve platform security, detect abuse, and maintain service quality.</li>
            <li><strong style="color: var(--text-primary)">Legal obligation (Art. 6(1)(c)):</strong> Where required to comply with applicable law.</li>
          </ul>
        </section>

        <section>
          <h2 class="section-title">4. How We Use Your Data</h2>
          <ul class="list-disc pl-5 space-y-1.5" style="color: var(--text-secondary)">
            <li>To create, manage, and secure your account.</li>
            <li>To facilitate communication between students and tutors via the chat feature.</li>
            <li>To display your public profile (name, biography, subjects, rating) to other users.</li>
            <li>To send transactional emails (account confirmation, password reset).</li>
            <li>To calculate and display tutor ratings.</li>
            <li>To investigate and prevent fraudulent or abusive behaviour.</li>
          </ul>
        </section>

        <section>
          <h2 class="section-title">5. Data Retention</h2>
          <p style="color: var(--text-secondary)">
            We retain your personal data for as long as your account is active. If you request deletion of
            your account, we will delete or anonymise your personal data within 30 days, unless we are required
            to retain it for longer under applicable law (e.g. tax or accounting obligations).
            Email confirmation tokens are deleted automatically after 15 minutes.
            Password reset codes are deleted immediately after a successful password reset.
          </p>
        </section>

        <section>
          <h2 class="section-title">6. Data Sharing</h2>
          <p class="mb-3" style="color: var(--text-secondary)">
            We do not sell your personal data. We may share your data with:
          </p>
          <ul class="list-disc pl-5 space-y-1.5" style="color: var(--text-secondary)">
            <li><strong style="color: var(--text-primary)">Resend:</strong> Our transactional email provider, used to send account confirmation and password reset emails. Data is processed in accordance with Resend's privacy policy.</li>
            <li><strong style="color: var(--text-primary)">Hosting providers:</strong> Cloud infrastructure providers used to operate the platform, bound by data processing agreements.</li>
            <li><strong style="color: var(--text-primary)">Law enforcement:</strong> Where required by law or to protect the rights and safety of our users.</li>
          </ul>
        </section>

        <section>
          <h2 class="section-title">7. Data Security</h2>
          <p style="color: var(--text-secondary)">
            We implement appropriate technical and organisational measures to protect your personal data against
            unauthorised access, alteration, disclosure, or destruction. Passwords are stored as bcrypt hashes
            and are never stored in plain text. Authentication uses short-lived JWT tokens. Despite these
            measures, no data transmission over the internet can be guaranteed to be 100% secure.
          </p>
        </section>

        <section>
          <h2 class="section-title">8. Your Rights under GDPR</h2>
          <p class="mb-3" style="color: var(--text-secondary)">
            As a data subject, you have the following rights under the GDPR:
          </p>
          <div class="space-y-2">
            <div v-for="right in gdprRights" :key="right.title"
                 class="flex gap-3 p-3 rounded-xl"
                 style="background: var(--bg-surface); border: 1px solid var(--border-subtle)">
              <span class="text-violet-500 font-bold text-sm mt-0.5">→</span>
              <div>
                <p class="text-sm font-semibold" style="color: var(--text-primary)">{{ right.title }}</p>
                <p class="text-sm" style="color: var(--text-secondary)">{{ right.desc }}</p>
              </div>
            </div>
          </div>
          <p class="mt-4 text-sm" style="color: var(--text-secondary)">
            To exercise any of these rights, please contact us at
            <a href="mailto:info.edurando@gmail.com" class="text-link">info.edurando@gmail.com</a>.
            You also have the right to lodge a complaint with the competent supervisory authority in Hamburg:
            <strong style="color: var(--text-primary)">Der Hamburgische Beauftragte für Datenschutz und Informationsfreiheit</strong>.
          </p>
        </section>

        <section>
          <h2 class="section-title">9. Cookies</h2>
          <p style="color: var(--text-secondary)">
            Edurando uses browser storage (localStorage and sessionStorage) to maintain your session and
            temporarily store in-progress flows such as registration confirmation. We do not use third-party
            tracking cookies or advertising cookies.
          </p>
        </section>

        <section>
          <h2 class="section-title">10. Changes to this Policy</h2>
          <p style="color: var(--text-secondary)">
            We may update this Privacy Policy from time to time. We will notify you of significant changes
            by displaying a notice on the platform or by sending you an email. Continued use of Edurando
            after changes take effect constitutes your acceptance of the revised policy.
          </p>
        </section>

        <section>
          <h2 class="section-title">11. Contact</h2>
          <p style="color: var(--text-secondary)">
            For any privacy-related questions or requests, please contact our data protection contact:
          </p>
          <div class="mt-3 p-4 rounded-xl" style="background: var(--bg-surface); border: 1px solid var(--border-subtle)">
            <a href="mailto:info.edurando@gmail.com" class="text-link">info.edurando@gmail.com</a>
          </div>
        </section>

      </div>
    </div>
  </div>
  <Footer />
</template>

<script setup>
import Footer from "@/modules/Footer/Footer.vue"

const gdprRights = [
  { title: 'Right of access (Art. 15)',        desc: 'You may request a copy of the personal data we hold about you.' },
  { title: 'Right to rectification (Art. 16)', desc: 'You may request correction of inaccurate or incomplete data.' },
  { title: 'Right to erasure (Art. 17)',        desc: 'You may request deletion of your personal data ("right to be forgotten").' },
  { title: 'Right to restriction (Art. 18)',   desc: 'You may request that we restrict processing of your data in certain circumstances.' },
  { title: 'Right to portability (Art. 20)',   desc: 'You may receive your data in a structured, machine-readable format.' },
  { title: 'Right to object (Art. 21)',         desc: 'You may object to processing based on legitimate interests.' },
  { title: 'Right to withdraw consent',         desc: 'Where processing is based on consent, you may withdraw it at any time without affecting prior lawful processing.' },
]
</script>

<style scoped>
.section-title {
  font-size: 17px;
  font-weight: 700;
  color: var(--text-primary);
  margin-bottom: 10px;
}
.text-link {
  color: #8b5cf6;
  text-decoration: none;
}
.text-link:hover {
  text-decoration: underline;
}
</style>
