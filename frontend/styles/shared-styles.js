// eagerly import theme styles so as we can override them
import '@vaadin/vaadin-lumo-styles/all-imports';

const $_documentContainer = document.createElement('template');

$_documentContainer.innerHTML = `
<custom-style>
  <style>
    html {
      --lumo-shade-5pct: rgba(33, 33, 33, 0.05);
      --lumo-shade-10pct: rgba(33, 33, 33, 0.1);
      --lumo-shade-20pct: rgba(33, 33, 33, 0.2);
      --lumo-shade-30pct: rgba(33, 33, 33, 0.3);
      --lumo-shade-40pct: rgba(33, 33, 33, 0.4);
      --lumo-shade-50pct: rgba(33, 33, 33, 0.5);
      --lumo-shade-60pct: rgba(33, 33, 33, 0.6);
      --lumo-shade-70pct: rgba(33, 33, 33, 0.7);
      --lumo-shade-80pct: rgba(33, 33, 33, 0.8);
      --lumo-shade-90pct: rgba(33, 33, 33, 0.9);
      --lumo-primary-color-50pct: rgba(235, 89, 5, 0.5);
      --lumo-primary-color-10pct: rgba(235, 89, 5, 0.1);
      --lumo-error-color-50pct: rgba(231, 24, 24, 0.5);
      --lumo-error-color-10pct: rgba(231, 24, 24, 0.1);
      --lumo-success-color-50pct: rgba(62, 229, 170, 0.5);
      --lumo-success-color-10pct: rgba(62, 229, 170, 0.1);
      --lumo-shade: hsl(0, 0%, 13%);
      --lumo-primary-color: hsl(22, 96%, 47%);
      --lumo-primary-text-color: hsl(22, 100%, 42%);
      --lumo-error-color: hsl(0, 81%, 50%);
      --lumo-error-text-color: hsl(0, 86%, 45%);
      --lumo-success-color: hsl(159, 76%, 57%);
      --lumo-success-contrast-color: hsl(159, 29%, 10%);
      --lumo-success-text-color: hsl(159, 61%, 40%);
      --lumo-font-family: Frutiger, "Frutiger Linotype", Univers, Calibri, "Gill Sans", "Gill Sans MT", "Myriad Pro", Myriad, "DejaVu Sans Condensed", "Liberation Sans", "Nimbus Sans L", Tahoma, Geneva, "Helvetica Neue", Helvetica, Arial, sans-serif;
    }
  </style>
</custom-style>


<custom-style>
  <style>
    html {
      overflow:hidden;
    }
  </style>
</custom-style>

<dom-module id="app-layout" theme-for="vaadin-app-layout">
  <template>
    <style>
      :host(:not([dir='rtl']):not([overlay])) [part='drawer'] {
        border-right: none;
        box-shadow: var(--lumo-box-shadow-s);
        background-color: var(--lumo-base-color);
        z-index: 1;
      }
      :host([dir='rtl']:not([overlay])) [part='drawer'] {
        border-left: none;
        box-shadow: var(--lumo-box-shadow-s);
        background-color: var(--lumo-base-color);
        z-index: 1;
      }
      [part='navbar'] {
        box-shadow: var(--lumo-box-shadow-s);
      }
    </style>
  </template>
</dom-module>`;

document.head.appendChild($_documentContainer.content);
