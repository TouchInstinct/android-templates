package ru.touchin.templates.pairs;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Pair that needed for saving in state because it implements Serializable interface.
 * Both arguments are not null.
 * Note that if you want to save this pair in state, you need make TFirst and TSecond Serializable too.
 *
 * @param <TFirst> type of the first nonnull argument.
 * @param <TSecond> type of the second nonnull argument.
 */
public class NonNullPair<TFirst, TSecond> implements Serializable {

    private static final long serialVersionUID = 1L;

    @NonNull
    private final TFirst first;
    @NonNull
    private final TSecond second;

    public NonNullPair(@NonNull final TFirst first, @NonNull final TSecond second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get first argument of this pair. It is always not null.
     */
    @NonNull
    public TFirst getFirst() {
        return first;
    }

    /**
     * Get second argument of this pair. It is always not null.
     */
    @NonNull
    public TSecond getSecond() {
        return second;
    }

}
