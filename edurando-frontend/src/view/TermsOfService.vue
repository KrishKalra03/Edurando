<template>
  <div class="min-h-screen pt-24 pb-16 px-4 sm:px-6 lg:px-8" style="background: var(--bg-base)">
    <div class="max-w-3xl mx-auto">

      <!-- Header -->
      <div class="mb-12">
        <p class="text-xs font-semibold uppercase tracking-widest mb-2" style="color: var(--text-muted)">Legal</p>
        <h1 class="text-4xl font-extrabold mb-3" style="color: var(--text-primary)">Terms of Service</h1>
        <p class="text-sm" style="color: var(--text-muted)">Last updated: 1 July 2024</p>
        <div class="mt-6 h-px" style="background: var(--border-subtle)"></div>
      </div>

      <div class="prose-section space-y-10">

        <section>
          <p style="color: var(--text-secondary)">
            Welcome to Edurando. By accessing or using our platform, you agree to be bound by these Terms of Service.
            Please read them carefully. If you do not agree with any part of these terms, you may not use our services.
          </p>
        </section>

        <section>
          <h2 class="section-title">1. About Edurando</h2>
          <p style="color: var(--text-secondary)">
            Edurando is an online marketplace that connects students seeking tutoring or academic support
            with independent tutors and educators. Edurando itself does not provide tutoring services
            directly; it provides the platform infrastructure through which tutors and students interact.
          </p>
        </section>

        <section>
          <h2 class="section-title">2. Eligibility</h2>
          <p style="color: var(--text-secondary)">
            You must be at least 16 years of age to create an account. Users under the age of 18 must have
            consent from a parent or legal guardian. By registering, you confirm that all information you
            provide is accurate, current, and complete.
          </p>
        </section>

        <section>
          <h2 class="section-title">3. User Accounts</h2>
          <p style="color: var(--text-secondary)">
            You are responsible for maintaining the confidentiality of your account credentials and for all
            activities that occur under your account. You agree to notify Edurando immediately at
            <a href="mailto:info.edurando@gmail.com" class="text-link">info.edurando@gmail.com</a> of any
            unauthorised use of your account. Edurando cannot and will not be liable for any loss or damage
            arising from your failure to comply with this obligation.
          </p>
        </section>

        <section>
          <h2 class="section-title">4. Acceptable Use</h2>
          <p class="mb-3" style="color: var(--text-secondary)">When using Edurando, you agree not to:</p>
          <ul class="list-disc pl-5 space-y-1.5" style="color: var(--text-secondary)">
            <li>Post false, misleading, or fraudulent content.</li>
            <li>Harass, threaten, or abuse other users.</li>
            <li>Upload malware, spam, or any harmful code.</li>
            <li>Attempt to gain unauthorised access to Edurando systems or another user's account.</li>
            <li>Reproduce or redistribute platform content without prior written consent from Edurando.</li>
            <li>Use the platform for any unlawful purpose or in violation of applicable laws.</li>
          </ul>
        </section>

        <section>
          <h2 class="section-title">5. Tutor Services</h2>
          <p style="color: var(--text-secondary)">
            Tutors on Edurando are independent service providers, not employees or agents of Edurando.
            Each tutor is solely responsible for the accuracy of their profile, qualifications, and the
            quality of services they provide. Edurando does not guarantee the outcome of any tutoring session
            and is not liable for disputes arising between students and tutors. Any contractual relationship
            for tutoring services is established directly between the student and the tutor.
          </p>
        </section>

        <section>
          <h2 class="section-title">6. Ratings and Reviews</h2>
          <p style="color: var(--text-secondary)">
            Users may leave ratings and reviews for tutors. You agree that all feedback submitted is honest,
            based on genuine experience, and does not violate any applicable law. Edurando reserves the right
            to remove any review that it considers fraudulent, abusive, or otherwise inappropriate. Opinions
            expressed in reviews do not represent the views of Edurando.
          </p>
        </section>

        <section>
          <h2 class="section-title">7. Intellectual Property</h2>
          <p style="color: var(--text-secondary)">
            All content, design, code, and materials on the Edurando platform — including but not limited to
            logos, graphics, and text — are the property of Edurando or its licensors and are protected
            by applicable copyright and trademark laws. You may not copy, reproduce, distribute, or create
            derivative works from any Edurando content without explicit written permission.
          </p>
        </section>

        <section>
          <h2 class="section-title">8. Availability and Modifications</h2>
          <p style="color: var(--text-secondary)">
            Edurando reserves the right to modify, suspend, or discontinue any part of the platform at any
            time without prior notice. We may also update these Terms of Service at any time. Continued use
            of the platform after changes are posted constitutes your acceptance of the revised terms.
          </p>
        </section>

        <section>
          <h2 class="section-title">9. Limitation of Liability</h2>
          <p style="color: var(--text-secondary)">
            To the fullest extent permitted by applicable law, Edurando shall not be liable for any
            indirect, incidental, special, consequential, or punitive damages arising from your use of or
            inability to use the platform, including damages for loss of data, revenue, or goodwill.
            Edurando's total liability for any claim shall not exceed the amount paid by you, if any, to
            Edurando in the twelve months preceding the claim.
          </p>
        </section>

        <section>
          <h2 class="section-title">10. Governing Law</h2>
          <p style="color: var(--text-secondary)">
            These Terms of Service are governed by and construed in accordance with the laws of the Federal
            Republic of Germany. Any disputes arising out of or in connection with these terms shall be
            subject to the exclusive jurisdiction of the courts in Hamburg, Germany.
          </p>
        </section>

        <section>
          <h2 class="section-title">11. Contact</h2>
          <p style="color: var(--text-secondary)">
            If you have any questions about these Terms of Service, please contact us:
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
