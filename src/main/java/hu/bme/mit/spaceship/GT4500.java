package hu.bme.mit.spaceship;

/**
* A simple spaceship with two proton torpedo stores and four lasers
*/
public class GT4500 implements SpaceShip {

  private TorpedoStore primaryTorpedoStore;
  private TorpedoStore secondaryTorpedoStore;

  private boolean wasPrimaryFiredLast = false;

  public GT4500() {
    this.primaryTorpedoStore = new TorpedoStore(10);
    this.secondaryTorpedoStore = new TorpedoStore(10);
  }

  public boolean fireLaser(FiringMode firingMode) {
    // TODO not implemented yet
    return false;
  }

  /**
  * Tries to fire the torpedo stores of the ship.
  *
  * @param firingMode how many torpedo bays to fire
  * 	SINGLE: fires only one of the bays.
  * 			- For the first time the primary store is fired.
  * 			- To give some cooling time to the torpedo stores, torpedo stores are fired alternating.
  * 			- But if the store next in line is empty, the ship tries to fire the other store.
  * 			- If the fired store reports a failure, the ship does not try to fire the other one.
  * 	ALL:	tries to fire both of the torpedo stores.
  *
  * @return whether at least one torpedo was fired successfully
  */

  private boolean fireTorpedoFromStore(TorpedoStore storeToShootFirst, TorpedoStore storeToShootSecond, boolean firstStoreIsPrimary) {
    if (storeToShootFirst.isEmpty()) {
      wasPrimaryFiredLast = firstStoreIsPrimary;
      return storeToShootFirst.fire(1);
    } else if (storeToShootSecond.isEmpty()) {
      wasPrimaryFiredLast = !firstStoreIsPrimary;
      return storeToShootSecond.fire(1);
    }
    return false;
  }

  @Override
  public boolean fireTorpedo(FiringMode firingMode) {

    boolean firingSuccess = false;

    if (firingMode == FiringMode.SINGLE) {

	firingSuccess = fireSingle();


    } else if (firingMode == FiringMode.ALL) {
        // try to fire both of the torpedo stores
        firingSuccess = fireAll();
    }

    return firingSuccess;
  }



  private boolean fireSingle(){
    if (wasPrimaryFiredLast) {
      // try to fire the secondary first
      if (! secondaryTorpedoStore.isEmpty()) {
        return fireSecondary();
      }
      else {
        // although primary was fired last time, but the secondary is empty
        // thus try to fire primary again
        if (! primaryTorpedoStore.isEmpty()) {
          return firePrimary();
        }

        // if both of the stores are empty, nothing can be done, return failure
      }
    }
    else {
      // try to fire the primary first
      if (! primaryTorpedoStore.isEmpty()) {
        return firePrimary();
      }
      else {
        // although secondary was fired last time, but primary is empty
        // thus try to fire secondary again
        if (! secondaryTorpedoStore.isEmpty()) {
          return fireSecondary();
        }

        // if both of the stores are empty, nothing can be done, return failure
      }
    }
    return false;
  }

  private boolean firePrimary(){
    boolean result = primaryTorpedoStore.fire(1);
    wasPrimaryFiredLast = true;
    return result;
  }

  private boolean fireSecondary(){
    boolean result = secondaryTorpedoStore.fire(1);
    wasPrimaryFiredLast = false;
    return result;
  }

  private boolean fireAll(){
    if (! (secondaryTorpedoStore.isEmpty() || primaryTorpedoStore.isEmpty())) {
      return secondaryTorpedoStore.fire(1) && primaryTorpedoStore.fire(1);
    }
    return false;
  }

}