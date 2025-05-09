import React, { useState } from "react";
import { groupsApi } from "../../api/groupsApi";
import styles from "./Group.module.scss";

interface Props {
  groupId: number;
  onTransactionAdded: () => void;
}

const AddGroupTransaction: React.FC<Props> = ({
  groupId,
  onTransactionAdded,
}) => {
  const [newDebtTitle, setNewDebtTitle] = useState("");
  const [amount, setAmount] = useState("");
  const [type, setType] = useState<"EXPENSE" | "INCOME">("EXPENSE");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!amount) return;

    try {
      await groupsApi.addGroupTransaction(
        groupId,
        parseFloat(amount),
        type,
        newDebtTitle
      );
      setAmount("");
      setType("EXPENSE");
      onTransactionAdded();
    } catch (error) {
      console.error("Błąd dodawania transakcji grupowej:", error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className={styles.form}>
      <div className="debt-container">
        <h3>Dodaj nowy {type === "EXPENSE" ? "wydatek" : "przychód"}</h3>
        <div className={styles.formsContainer}>
          <input
            type="text"
            placeholder="Tytuł"
            value={newDebtTitle}
            onChange={(e) => setNewDebtTitle(e.target.value)}
            className={styles.input}
          />

          <input
            type="number"
            placeholder="Kwota"
            value={amount}
            onChange={(e) => setAmount(e.target.value)}
            className={styles.input}
          />

          <select
            value={type}
            onChange={(e) => setType(e.target.value as "EXPENSE" | "INCOME")}
            className={styles.input}
          >
            <option value="">-- wybierz --</option>
            <option value="EXPENSE">Wydatek</option>
            <option value="INCOME">Przychód</option>
          </select>
          <div className={styles.formGroup}></div>
          <button type="submit" className={styles.button}>
            Dodaj
          </button>
        </div>
      </div>
    </form>
  );
};

export default AddGroupTransaction;
