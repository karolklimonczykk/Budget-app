import graphqlClient from "./graphClient";

export const groupsApi = {
  getGroups: async () => {
    const query = `
    query {
      myGroups {
        id
        name
        ownerId
      }
    }
  `;
    const response = await graphqlClient(query);
    return response.data.myGroups;
  },

  createGroup: async (name: string) => {
    const mutation = `
      mutation CreateGroup($groupDTO: GroupInput!) {
        createGroup(groupDTO: $groupDTO) {
          id
          name
        }
      }
    `;
    return graphqlClient(mutation, { groupDTO: { name } });
  },

  getGroupMembers: async (groupId: number) => {
    const query = `
      query($groupId: ID!) {
        groupMembers(groupId: $groupId) {
          id
          userId
          groupId
          userEmail
        }
      }
    `;
    const response = await graphqlClient(query, { groupId });
    return response.data.groupMembers;
  },

  addMember: async (groupId: number, userEmail: string) => {
    const mutation = `
      mutation($membershipDTO: MembershipInput!) {
        addMember(membershipDTO: $membershipDTO) {
          id
          groupId
          userId
        }
      }
    `;
    return graphqlClient(mutation, { membershipDTO: { groupId, userEmail } });
  },

  deleteGroup: async (id: number) => {
    const mutation = `
      mutation($id: ID!) {
        deleteGroup(id: $id)
      }
    `;
    return graphqlClient(mutation, { id });
  },

  removeMember: async (membershipId: number) => {
    const mutation = `
      mutation($membershipId: ID!) {
        removeMember(membershipId: $membershipId)
      }
    `;
    return graphqlClient(mutation, { membershipId });
  },

  addGroupTransaction: async (
    groupId: number,
    amount: number,
    type: string,
    title: string
  ) => {
    const mutation = `
      mutation($groupTransactionDTO: GroupTransactionInput!) {
        addGroupTransaction(groupTransactionDTO: $groupTransactionDTO)
      }
    `;
    return graphqlClient(mutation, {
      groupTransactionDTO: { groupId, amount, type, title },
    });
  },

  getDebts: async (groupId: number) => {
    const query = `
     query($groupId: ID!) {
        groupDebts(groupId: $groupId) {
            id
            title
            amount
            debtor {
            email
            }
            creditor {
            email
            }
        }
    }
    `;
    const response = await graphqlClient(query, { groupId });
    return response.data.groupDebts;
  },
};
