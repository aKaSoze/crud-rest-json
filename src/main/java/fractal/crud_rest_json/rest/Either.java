package fractal.crud_rest_json.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class Either<L, R> {

	public enum Type {
		LEFT, RIGHT;
	}

	public abstract Type getType();

	public Left<L, R> asLeft() {
		return (Left<L, R>) this;
	}

	public Right<L, R> asRight() {
		return (Right<L, R>) this;
	}

	public <LE, RI> Either<LE, RI> map(Function<? super L, ? extends LE> leftMapper, Function<? super R, ? extends RI> rightMapper) {
		Objects.requireNonNull(leftMapper);
		Objects.requireNonNull(rightMapper);
		return flatMap(l -> new Left<>(leftMapper.apply(l)), r -> new Right<>(rightMapper.apply(r)));
	}

	public <LE, RI> Either<LE, RI> flatMap(Function<? super L, Either<LE, RI>> leftMapper, Function<? super R, Either<LE, RI>> rightMapper) {
		Objects.requireNonNull(leftMapper);
		Objects.requireNonNull(rightMapper);

		switch (getType()) {
		case LEFT:
			return leftMapper.apply(asLeft().get());
		case RIGHT:
			return rightMapper.apply(asRight().get());
		}

		throw new IllegalArgumentException();
	}

	public <T> T unify(Function<? super L, ? extends T> leftMapper, Function<? super R, ? extends T> rightMapper) {
		Objects.requireNonNull(leftMapper);
		Objects.requireNonNull(rightMapper);

		switch (getType()) {
		case LEFT:
			return leftMapper.apply(asLeft().get());
		case RIGHT:
			return rightMapper.apply(asRight().get());
		}

		throw new IllegalArgumentException();
	}

	public <T> T unify(Function<Object, ? extends T> genericMapper) {
		return unify(genericMapper, genericMapper);
	}

	public Optional<Either<L, R>> filter(Predicate<? super L> leftPredicate, Predicate<? super R> rightPredicate) {
		Objects.requireNonNull(leftPredicate);
		Objects.requireNonNull(rightPredicate);

		switch (getType()) {
		case LEFT:
			return leftPredicate.test(asLeft().get()) ? Optional.of(this) : Optional.empty();
		case RIGHT:
			return rightPredicate.test(asRight().get()) ? Optional.of(this) : Optional.empty();
		}

		throw new IllegalArgumentException();
	}

	@Override
	public String toString() {
		return unify(o -> getType() + ": " + o.getClass().getSimpleName() + " -> " + o.toString());
	}

	public static class Left<L, R> extends Either<L, R> {
		private final L	value;

		public Left(L value) {
			this.value = value;
		}

		public L get() {
			return value;
		}

		@Override
		public Type getType() {
			return Type.LEFT;
		}
	}

	public static class Right<L, R> extends Either<L, R> {
		private final R	value;

		public Right(R value) {
			this.value = value;
		}

		public R get() {
			return value;
		}

		@Override
		public Type getType() {
			return Type.RIGHT;
		}
	}

	public static void main(String[] args) {
		List<Either<String, Long>> eithers = new ArrayList<>();

		eithers.add(new Left<String, Long>("some string"));
		eithers.add(new Right<String, Long>(7L));

		for (Either<String, Long> either : eithers) {
			switch (either.getType()) {
			case LEFT:
				System.out.println("String is: " + either.asLeft().get());
				break;
			case RIGHT:
				System.out.println("Long is: " + either.asRight().get());
				break;
			}
		}
	}

}
