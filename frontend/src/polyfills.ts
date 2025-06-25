import { Buffer } from 'buffer';

(window as any).global = window;
(window as any).Buffer = Buffer;

// Adaugă polyfill pentru process.env (dacă este necesar pentru alte biblioteci)
(window as any).process = { env: { DEBUG: undefined } };

console.log('Polyfills loaded:', !!(window as any).global && !!(window as any).Buffer);