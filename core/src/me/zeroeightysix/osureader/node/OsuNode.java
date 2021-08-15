package me.zeroeightysix.osureader.node;

import java.io.Serializable;

/**
 * Created by 086 on 16/05/2018.
 */
public interface OsuNode<T> extends Serializable {

     T getValue();

}
