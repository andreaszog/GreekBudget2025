# GreekBudget2025

Εφαρμογή JavaFX για την ανάλυση και παρουσίαση στοιχείων Κρατικού Προϋπολογισμού.
Το project αναπτύχθηκε στο πλαίσιο πανεπιστημιακής εργασίας και περιλαμβάνει
οπτικοποίηση δεδομένων, σενάρια προϋπολογισμού και εξαγωγή αναφορών.

---

## 🔧 Τεχνολογίες
- Java 20
- JavaFX
- Maven
- SQLite
- Apache PDFBox

---

## 🧪 Testing & Quality Assurance

Στο project έχουν ενσωματωθεί εργαλεία ελέγχου ποιότητας κώδικα:

### ✅ Unit Tests
- Χρήση **JUnit 5**
- Υπάρχουν βασικά unit tests για βοηθητικές κλάσεις (π.χ. `MoneyUtil`)

### 📊 Code Coverage (JaCoCo)
- Χρήση **JaCoCo**
- Δημιουργείται HTML report μετά την εκτέλεση:
  ```bash
  mvn clean verify
