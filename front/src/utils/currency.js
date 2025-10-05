// Utilitários para formatação de moeda brasileira

/**
 * Formata um valor numérico para moeda brasileira
 * @param {number|string} value - Valor a ser formatado
 * @returns {string} - Valor formatado (ex: "3,50")
 */
export function formatCurrency(value) {
  const numValue = Number(value) || 0;
  return numValue.toFixed(2).replace('.', ',');
}

/**
 * Formata um valor numérico para moeda brasileira com símbolo R$
 * @param {number|string} value - Valor a ser formatado
 * @returns {string} - Valor formatado com símbolo (ex: "R$ 3,50")
 */
export function formatCurrencyWithSymbol(value) {
  return `R$ ${formatCurrency(value)}`;
}

/**
 * Converte uma string de moeda brasileira para número
 * @param {string} currencyString - String de moeda (ex: "3,50" ou "R$ 3,50")
 * @returns {number} - Valor numérico
 */
export function parseCurrency(currencyString) {
  if (typeof currencyString === 'number') {
    return currencyString;
  }
  
  const cleanString = String(currencyString)
    .replace('R$', '')
    .replace(/\s/g, '')
    .replace(',', '.');
    
  return parseFloat(cleanString) || 0;
}

/**
 * Formata um valor usando a API Intl.NumberFormat para moeda brasileira
 * @param {number|string} value - Valor a ser formatado
 * @returns {string} - Valor formatado (ex: "R$ 3,50")
 */
export function formatCurrencyIntl(value) {
  const numValue = Number(value) || 0;
  return new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL'
  }).format(numValue);
}

export default {
  formatCurrency,
  formatCurrencyWithSymbol,
  parseCurrency,
  formatCurrencyIntl
};
