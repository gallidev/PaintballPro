package networking.game;

import physics.PowerupType;

/**
 * Listener class to game updates, in order to make the ServerGameStateSender Event based and avoiding continuously sending packets
 *
 * @author Filippo Galli
 * @author Artur Komoter
 *
 */

public interface GameUpdateListener
{
	void onFlagCaptured(int player);
	void onFlagDropped(int player);
	void onFlagRespawned(int player);
	void onPowerupAction(PowerupType type, int player);
	void onPowerupRespawn(PowerupType type, int location);
	void onShotBullet(int playerId, int bulletId, double x, double y, double angle);
	void onBulletKills(int playerId, int bulletId);

}
