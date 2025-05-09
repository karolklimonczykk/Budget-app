/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable @typescript-eslint/no-explicit-any */
/* src/pages/GroupMembersPage/GroupMembersPage.tsx */
import React, { useEffect, useState } from "react";
import { groupsApi } from "../../api/groupsApi";
import { useAuth } from "../../context/AuthContext"; // <== DODAJ ten import
import styles from "./Group.module.scss";
import AddGroupTransaction from "./AddGroupTransaction";

interface Group {
  id: number;
  name: string;
  ownerId: number;
}

interface Member {
  id: number;
  userId: number;
  groupId: number;
  userEmail: string;
}

interface Props {
  group: Group;
  onBack: () => void;
}

interface Debt {
  id: number;
  debtor: { email: string };
  creditor: { email: string };
  amount: number;
  title: string;
}

const GroupMembersPage: React.FC<Props> = ({ group, onBack }) => {
  const { user } = useAuth(); // <== DODAJ to!
  const [members, setMembers] = useState<Member[]>([]);
  const [newMemberEmail, setNewMemberEmail] = useState("");
  const [debts, setDebts] = useState<Debt[]>([]);

  useEffect(() => {
    fetchMembers();
  }, [group]);

  const fetchMembers = async () => {
    const data = await groupsApi.getGroupMembers(group.id);
    const debtsData = await groupsApi.getDebts(group.id);
    setDebts(debtsData);
    setMembers(data);
  };

  const handleAddMember = async () => {
    try {
      await groupsApi.addMember(group.id, newMemberEmail);
      setNewMemberEmail("");
      fetchMembers();
    } catch (error: any) {
      console.error("Błąd dodawania członka:", error);
      alert(error.message || "Wystąpił błąd.");
    }
  };

  const handleRemove = async (id: number) => {
    await groupsApi.removeMember(id);
    fetchMembers();
  };

  return (
    <div className={styles.container}>
      <button onClick={onBack} className={styles.backButton}>
        Wróć do grup
      </button>
      <h2>Członkowie grupy: {group.name}</h2>

      <div className={styles.form}>
        <input
          type="text"
          placeholder="Email użytkownika"
          value={newMemberEmail}
          onChange={(e) => setNewMemberEmail(e.target.value)}
        />
        <button onClick={handleAddMember}>Dodaj członka</button>
      </div>

      <AddGroupTransaction
        groupId={group.id}
        onTransactionAdded={fetchMembers}
      />
      <ul className={styles.memberList}>
        {members.map((member) => (
          <li key={member.id}>
            {member.userEmail}
            {member.userId === group.ownerId && (
              <>
                <span className={styles.adminLabel}>(admin)</span>
              </>
            )}
            {user?.id == group.ownerId && member.userId !== group.ownerId && (
              <button onClick={() => handleRemove(member.id)}>Usuń</button>
            )}
          </li>
        ))}
      </ul>
      {debts.length > 0 && (
        <div className={styles.debtsSection}>
          <h3>Długi w grupie:</h3>
          <ul className={styles.debtsList}>
            {debts.map((debt) => (
              <li key={debt.id}>
                <strong>{debt.debtor.email}</strong> jest winien{" "}
                <strong>{debt.creditor.email}</strong> {debt.amount.toFixed(2)}{" "}
                zł <strong>za {debt.title}</strong>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default GroupMembersPage;
