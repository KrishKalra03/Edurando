<template>
  <div class="min-h-screen pt-24 pb-16 px-4 sm:px-6 lg:px-8" style="background: var(--bg-base)">
    <div class="max-w-3xl mx-auto">

      <!-- Header -->
      <div class="mb-12">
        <p class="text-xs font-semibold uppercase tracking-widest mb-2" style="color: var(--text-muted)">Legal</p>
        <h1 class="text-4xl font-extrabold mb-3" style="color: var(--text-primary)">Imprint</h1>
        <p class="text-sm" style="color: var(--text-muted)">Information according to § 5 TMG</p>
        <div class="mt-6 h-px" style="background: var(--border-subtle)"></div>
      </div>

      <div class="space-y-10">

        <section>
          <h2 class="section-title">Responsible for content</h2>
          <div class="p-5 rounded-xl" style="background: var(--bg-surface); border: 1px solid var(--border-subtle)">
            <p class="font-semibold text-base mb-1" style="color: var(--text-primary)">Krish Kalra</p>
            <p style="color: var(--text-secondary)">Holzhäuser 16d</p>
            <p style="color: var(--text-secondary)">21079 Hamburg</p>
            <p style="color: var(--text-secondary)">Germany</p>
          </div>
        </section>

        <section>
          <h2 class="section-title">Contact</h2>
          <div class="p-5 rounded-xl space-y-1" style="background: var(--bg-surface); border: 1px solid var(--border-subtle)">
            <a href="mailto:kontakt@devvault.de" class="text-link block">kontakt@devvault.de</a>
            <a href="tel:+4915679818930" class="text-link block">+49 15679 818930</a>
          </div>
        </section>

        <section>
          <h2 class="section-title">Disclaimer</h2>
          <div class="space-y-6">
            <div>
              <h3 class="subsection-title">Liability for content</h3>
              <p style="color: var(--text-secondary)">
                The contents of this platform have been created with the utmost care. However, we cannot
                guarantee the accuracy, completeness, or timeliness of the content. As a service provider,
                we are responsible for our own content on this platform under general law. However, we are
                not obligated to monitor transmitted or stored third-party information or to investigate
                circumstances that indicate illegal activity.
              </p>
            </div>
            <div>
              <h3 class="subsection-title">Liability for links</h3>
              <p style="color: var(--text-secondary)">
                Our platform contains links to external third-party websites over whose content we have no
                influence. We therefore cannot accept any liability for this external content. The respective
                provider or operator of the linked pages is always responsible for their content.
              </p>
            </div>
            <div>
              <h3 class="subsection-title">Copyright</h3>
              <p style="color: var(--text-secondary)">
                The content and works created by the site operator on this platform are subject to German
                copyright law. Reproduction, editing, distribution, or any kind of use beyond the limits of
                copyright law requires the written consent of the respective author or creator.
              </p>
            </div>
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
.subsection-title {
  font-size: 14px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 6px;
}
.text-link {
  color: #8b5cf6;
  text-decoration: none;
}
.text-link:hover {
  text-decoration: underline;
}
</style>
