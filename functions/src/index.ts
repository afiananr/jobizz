import {onUserCreated, AuthUserCreatedEvent} from "firebase-functions/v2/auth";
import {setGlobalOptions} from "firebase-functions/v2";
import * as logger from "firebase-functions/logger";
import * as admin from "firebase-admin";

admin.initializeApp();
const db = admin.firestore();

setGlobalOptions({region: "asia-southeast2"});

export const createWelcomeChat = onUserCreated(
  async (event: AuthUserCreatedEvent) => {
    const user = event.data;
    logger.log("New user registered:", user.uid, user.email);

    const newUser = {
      uid: user.uid,
      name: user.displayName || "Pengguna Baru",
      photoUrl: user.photoURL || `https://i.pravatar.cc/150?u=${user.uid}`,
    };

    const adminUser = {
      uid: "system_admin",
      name: "Admin Loker App",
      photoUrl: "https://i.imgur.com/k1d5Bv7.png",
    };

    const conversationData = {
      participantIds: [newUser.uid, adminUser.uid],
      participantNames: {
        [newUser.uid]: newUser.name,
        [adminUser.uid]: adminUser.name,
      },
      participantPhotos: {
        [newUser.uid]: newUser.photoUrl,
        [adminUser.uid]: adminUser.photoUrl,
      },
      lastMessage: "Selamat datang di Loker App! ✨",
      lastMessageTimestamp: admin.firestore.FieldValue.serverTimestamp(),
    };

    const conversationRef = await db.collection("conversations")
      .add(conversationData);
    logger.log("Welcome conversation document created:", conversationRef.id);

    const firstMessage = {
      senderId: adminUser.uid,
      text: "Selamat datang! Kami siap membantu Anda menemukan " +
            "pekerjaan impian. Jelajahi semua fitur yang ada.",
      timestamp: admin.firestore.FieldValue.serverTimestamp(),
    };

    await conversationRef.collection("messages").add(firstMessage);
    logger.log("First welcome message sent successfully.");

    return null;
  },
);
