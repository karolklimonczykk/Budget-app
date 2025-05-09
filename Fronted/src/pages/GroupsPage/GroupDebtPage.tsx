/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-explicit-any */
import React, { useEffect, useState } from "react";
import { groupsApi } from "../../api/groupsApi";
import { useParams } from "react-router-dom";
import styles from "./Group.module.scss";

const GroupDebtsPage: React.FC = () => {
  const { groupId } = useParams();
  const [debts, setDebts] = useState([]);

  useEffect(() => {
    fetchDebts();
  }, []);

  const fetchDebts = async () => {
    const data = await groupsApi.getDebts(Number(groupId));
    setDebts(data);
  };

  return (
    <div className={styles.container}>
      <h2>Długi w grupie</h2>

      <ul className={styles.list}>
        {debts.map((debt: any) => (
          <li key={debt.id}>
            Użytkownik {debt.fromUserId} musi oddać użytkownikowi{" "}
            {debt.toUserId} kwotę {debt.amount.toFixed(2)} zł
          </li>
        ))}
      </ul>
    </div>
  );
};

export default GroupDebtsPage;
